# Arquitetura - MBLRemote Control Android

## Visão Geral

O MBLRemote Control segue a arquitetura **MVVM** (Model-View-ViewModel) com princípios de **Clean Architecture** para garantir código modular, testável e fácil de manter.

## Camadas da Arquitetura

```
┌─────────────────────────────────────────┐
│         UI Layer (Jetpack Compose)      │
│  - Screens (Telas)                      │
│  - Components (Componentes)             │
│  - Theme (Tema e Cores)                 │
└────────────┬────────────────────────────┘
             │
┌────────────▼────────────────────────────┐
│      ViewModel Layer                    │
│  - ConnectionViewModel                  │
│  - BillingViewModel (Premium)           │
│  - SettingsViewModel                    │
└────────────┬────────────────────────────┘
             │
┌────────────▼────────────────────────────┐
│      Repository Layer                   │
│  - ConnectionRepository                 │
│  - BillingRepository                    │
│  - PreferencesRepository                │
└────────────┬────────────────────────────┘
             │
┌────────────▼────────────────────────────┐
│      Data Layer                         │
│  - Network (SocketClient)               │
│  - Local (DataStore)                    │
│  - Models (Data Classes)                │
└─────────────────────────────────────────┘
```

## Componentes Principais

### 1. UI Layer (Apresentação)

**Responsabilidade:** Renderizar a interface do usuário e capturar interações.

**Componentes:**
- **Screens:** `OnboardingScreen`, `ConnectionScreen`, `ControlScreen`
- **Components:** `DirectionalPad`, `VolumeControl`, `ConnectionStatus`
- **Theme:** Cores, tipografia, estilos globais

**Tecnologia:** Jetpack Compose (UI declarativa)

```kotlin
@Composable
fun ControlScreen(
    connectionState: ConnectionState,
    onDirectionalPress: (DirectionalKey) -> Unit,
    // ...
) {
    // Renderizar UI baseado no estado
}
```

### 2. ViewModel Layer

**Responsabilidade:** Gerenciar estado da UI e lógica de apresentação.

**Características:**
- Sobrevive a mudanças de configuração (rotação de tela)
- Expõe `StateFlow` para reatividade
- Não contém referências de UI

**Exemplo:**
```kotlin
class ConnectionViewModel(
    private val repository: ConnectionRepository
) : ViewModel() {
    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Disconnected)
    val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    fun connect(ip: String, port: Int) {
        // Lógica de conexão
    }
}
```

### 3. Repository Layer

**Responsabilidade:** Abstrair fontes de dados (rede, local storage).

**Padrão:** Repository Pattern
- Centraliza lógica de acesso a dados
- Permite trocar implementações facilmente
- Facilita testes (mock repositories)

**Exemplo:**
```kotlin
class ConnectionRepository(
    private val socketClient: SocketClient,
    private val dataStore: PreferencesDataStore
) {
    suspend fun connect(ip: String, port: Int): RemoteDevice {
        // Conectar via socket
        // Salvar dispositivo localmente
    }
}
```

### 4. Data Layer

**Responsabilidade:** Acessar dados de diferentes fontes.

#### 4.1 Network (SocketClient)

Comunicação com o App Companion via Socket TCP:

```kotlin
class SocketClient {
    suspend fun connect(ip: String, port: Int)
    suspend fun sendCommand(command: RemoteCommand)
    suspend fun disconnect()
}
```

#### 4.2 Local Storage (DataStore)

Armazenamento de preferências usando Jetpack DataStore:

```kotlin
interface PreferencesDataStore {
    suspend fun getRecentDevices(): List<RemoteDevice>
    suspend fun saveRecentDevice(device: RemoteDevice)
    suspend fun getUserPreferences(): UserPreferences
}
```

#### 4.3 Models

Data classes que representam o domínio:

```kotlin
data class RemoteDevice(
    val id: String,
    val name: String,
    val ip: String,
    val port: Int,
    val isConnected: Boolean
)

enum class CommandType {
    UP, DOWN, LEFT, RIGHT, OK, BACK, HOME, VOLUME_UP, VOLUME_DOWN
}

data class RemoteCommand(
    val type: CommandType,
    val timestamp: Long,
    val payload: String?
)
```

## Fluxo de Dados

### Exemplo: Enviar Comando de Navegação

```
User taps UP button
        ↓
UI calls onDirectionalPress(DirectionalKey.UP)
        ↓
ViewModel.sendCommand(RemoteCommand(CommandType.UP))
        ↓
Repository.sendCommand(command)
        ↓
SocketClient.sendCommand(command)
        ↓
Serializar para JSON e enviar via Socket
        ↓
App Companion recebe e executa KeyEvent
```

### Exemplo: Conectar a Dispositivo

```
User enters IP and taps Connect
        ↓
ViewModel.connect(ip, port)
        ↓
Repository.connect(ip, port)
        ↓
SocketClient.connect(ip, port)
        ↓
Validar conexão
        ↓
Salvar dispositivo em DataStore
        ↓
Atualizar _connectionState
        ↓
UI recompõe com novo estado
```

## Gerenciamento de Estado

### StateFlow vs MutableStateFlow

```kotlin
// ViewModel expõe apenas StateFlow (read-only)
private val _connectionState = MutableStateFlow<ConnectionState>(...)
val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

// UI coleta o estado
val state by viewModel.connectionState.collectAsState()
```

### Coroutines e Dispatchers

```kotlin
viewModelScope.launch {
    // Código de UI (Main thread)
    val result = repository.connect(ip, port)  // Dispatchers.IO
    // Atualizar estado (Main thread)
    _connectionState.value = ConnectionState.Connected(result)
}
```

## Injeção de Dependência

Atualmente, as dependências são criadas manualmente no `MainActivity`:

```kotlin
val socketClient = remember { SocketClient() }
val dataStore = remember { PreferencesDataStoreImpl(context) }
val repository = remember { ConnectionRepository(socketClient, dataStore) }
```

**Próximo passo:** Implementar Hilt para injeção automática.

## Padrões de Design Utilizados

### 1. Repository Pattern
Abstrai acesso a dados de múltiplas fontes.

### 2. Observer Pattern
`StateFlow` para reatividade e observação de mudanças.

### 3. Factory Pattern
ViewModelFactory para criar ViewModels com dependências.

### 4. Singleton Pattern
SocketClient e DataStore como singletons.

## Testabilidade

### Testes Unitários

```kotlin
@Test
fun testConnectWithValidIp() {
    val mockRepository = mockk<ConnectionRepository>()
    val viewModel = ConnectionViewModel(mockRepository)
    
    viewModel.connect("192.168.1.100", 5000)
    
    verify { mockRepository.connect("192.168.1.100", 5000) }
}
```

### Testes de Integração

```kotlin
@Test
fun testConnectionFlow() = runTest {
    val repository = ConnectionRepository(socketClient, dataStore)
    val device = repository.connect("192.168.1.100", 5000)
    
    assertTrue(device.isConnected)
}
```

## Escalabilidade

### Adicionar Nova Funcionalidade

1. **Criar Model:** `data class NewFeature(...)`
2. **Criar ViewModel:** `class NewFeatureViewModel(...)`
3. **Criar Repository:** `class NewFeatureRepository(...)`
4. **Criar Screen:** `@Composable fun NewFeatureScreen(...)`
5. **Integrar em Navigation:** Adicionar rota em `NavHost`

### Exemplo: Adicionar Touchpad (Premium)

```kotlin
// 1. Model
data class TouchpadEvent(val x: Float, val y: Float)

// 2. ViewModel
class TouchpadViewModel(repository: ConnectionRepository) : ViewModel() {
    fun sendMouseMove(x: Float, y: Float) { ... }
}

// 3. Repository
fun sendMouseMove(x: Float, y: Float) {
    socketClient.sendCommand(RemoteCommand(MOUSE_MOVE, payload = "$x,$y"))
}

// 4. Screen
@Composable
fun TouchpadScreen(viewModel: TouchpadViewModel) { ... }

// 5. Navigation
composable("touchpad") { TouchpadScreen(...) }
```

## Performance

### Otimizações Implementadas

1. **Lazy Composition:** Componentes renderizam apenas quando necessário
2. **State Hoisting:** Estado centralizado em ViewModel
3. **Coroutines:** Operações de rede em thread separada
4. **Caching:** Dispositivos recentes armazenados localmente

### Monitoramento

- Usar Android Profiler para monitorar:
  - CPU usage
  - Memory usage
  - Network traffic

## Segurança

### Boas Práticas

1. **Comunicação Local:** Socket restrito à rede local
2. **Validação de Input:** Validar IP antes de conectar
3. **Permissões:** Solicitar apenas permissões necessárias
4. **DataStore:** Dados sensíveis criptografados

### Próximos Passos

- Implementar SSL/TLS para comunicação
- Adicionar autenticação (token)
- Validar certificados

## Migração do React Native

### Mapeamento de Conceitos

| React Native | Android Nativo |
|---|---|
| Context API | ViewModel + StateFlow |
| useState | MutableStateFlow |
| useEffect | LaunchedEffect |
| Custom Hooks | ViewModel methods |
| Navigation Stack | Jetpack Navigation |
| AsyncStorage | DataStore |
| Componentes React | Composables |

### Reutilização de Lógica

- Modelos de dados (Models.kt)
- Lógica de validação (validation.ts → Kotlin)
- Constantes (theme, colors)
- Tipos de comandos

## Próximas Melhorias

1. **Hilt Dependency Injection**
2. **Unit Tests com Mockk**
3. **Integration Tests com Testcontainers**
4. **CI/CD Pipeline (GitHub Actions)**
5. **Crash Reporting (Firebase Crashlytics)**
6. **Analytics (Firebase Analytics)**
7. **SSL/TLS Communication**
8. **Offline Mode com Room Database**

---

**Última atualização:** Fevereiro 2026
