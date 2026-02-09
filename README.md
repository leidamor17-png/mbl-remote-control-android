# MBLRemote Control - Android Nativo

Aplicativo Android nativo para controlar projetores MBL via Wi-Fi usando **Kotlin** e **Jetpack Compose**.

## Arquitetura

O projeto segue a arquitetura **MVVM** (Model-View-ViewModel) com separação clara de responsabilidades:

```
app/src/main/kotlin/com/manus/mblremote/
├── ui/
│   ├── screens/              # Telas da aplicação
│   │   ├── OnboardingScreen.kt
│   │   ├── ConnectionScreen.kt
│   │   └── ControlScreen.kt
│   ├── components/           # Componentes reutilizáveis
│   │   ├── DirectionalPad.kt
│   │   ├── VolumeControl.kt
│   │   └── ConnectionStatus.kt
│   └── theme/                # Tema e cores
│       ├── Color.kt
│       ├── Type.kt
│       └── Theme.kt
├── viewmodel/                # ViewModels
│   └── ConnectionViewModel.kt
├── repository/               # Repositórios (camada de dados)
│   └── ConnectionRepository.kt
├── network/                  # Comunicação de rede
│   └── SocketClient.kt
├── data/                     # Armazenamento local
│   └── PreferencesDataStoreImpl.kt
├── model/                    # Modelos de dados
│   └── Models.kt
└── MainActivity.kt           # Entrada da aplicação
```

## Tecnologias Utilizadas

- **Kotlin** - Linguagem de programação
- **Jetpack Compose** - Framework de UI declarativa
- **Jetpack Navigation** - Navegação entre telas
- **Jetpack DataStore** - Armazenamento de preferências
- **Coroutines** - Programação assíncrona
- **Socket** - Comunicação via rede local
- **Google Play Billing** - Sistema de compras (Premium)

## Requisitos

- Android Studio Giraffe ou superior
- Android SDK 24+ (API 24)
- Kotlin 1.9.22+
- Gradle 8.2.0+

## Configuração e Compilação

### 1. Clonar o repositório

```bash
git clone <repository-url>
cd mbl-remote-control-android
```

### 2. Abrir no Android Studio

```bash
# Abrir o projeto
open -a "Android Studio" .
```

### 3. Sincronizar dependências

No Android Studio:
- File → Sync Now
- Aguarde a sincronização das dependências do Gradle

### 4. Compilar e executar

#### No emulador:
```bash
# Compilar e executar no emulador padrão
./gradlew installDebug
```

#### Em dispositivo físico:
```bash
# Conectar dispositivo via USB e executar
./gradlew installDebug
```

#### Build Release (APK):
```bash
# Gerar APK de produção
./gradlew assembleRelease
```

#### Build Release (AAB - Android App Bundle):
```bash
# Gerar AAB para publicação na Play Store
./gradlew bundleRelease
```

## Estrutura de Funcionalidades

### Versão Grátis
- ✅ Onboarding com 4 slides
- ✅ Conexão por IP local
- ✅ D-Pad (↑ ↓ ← → OK)
- ✅ Botões Voltar e Home
- ✅ Controle de Volume (+ / -)
- ✅ Feedback visual e háptico
- ✅ Histórico de dispositivos recentes

### Versão Premium
- 🔒 Touchpad (mouse remoto)
- 🔒 Teclado remoto
- 🔒 Atalhos rápidos (YouTube, Netflix, HDMI)
- 🔒 Temas premium
- 🔒 Sem anúncios

## Fluxo de Navegação

```
Onboarding (4 slides)
    ↓
Conexão (IP + Porta)
    ↓
Controle Remoto (D-Pad + Volume)
    ↓
Desconectar
```

## Comunicação com o App Companion

O aplicativo se comunica com o **App Companion** (instalado no projetor) via **Socket TCP** na rede local:

### Protocolo de Comunicação

```json
{
  "type": "UP",
  "timestamp": 1234567890,
  "payload": null
}
```

### Tipos de Comando

- `UP`, `DOWN`, `LEFT`, `RIGHT` - Navegação
- `OK` - Confirmar
- `BACK` - Voltar
- `HOME` - Ir para home
- `VOLUME_UP`, `VOLUME_DOWN` - Controle de volume
- `MOUSE_MOVE` - Movimento do mouse (Premium)
- `TEXT_INPUT` - Injeção de texto (Premium)
- `SHORTCUT_*` - Atalhos rápidos (Premium)

## Permissões Necessárias

O aplicativo requer as seguintes permissões:

- `INTERNET` - Comunicação via rede
- `ACCESS_NETWORK_STATE` - Verificar estado da rede
- `CHANGE_NETWORK_STATE` - Gerenciar conexões
- `VIBRATE` - Feedback háptico

## Configuração de Temas

O aplicativo suporta múltiplos temas:

### Tema Padrão (Escuro Profundo)
- Fundo: `#0F0F1E`
- Accent Primário: `#7C3AED` (Roxo)
- Accent Secundário: `#3B82F6` (Azul)

### Temas Premium
- Neon
- Minimalista
- Oceano

## Armazenamento Local

O aplicativo usa **DataStore** para armazenar:

- Preferências do usuário (onboarding, tema, feedback háptico)
- Histórico de dispositivos recentes
- Status de billing/Premium

## Integração com Google Play Billing

Para integrar compras in-app:

1. Configurar produto no Google Play Console
2. Implementar `BillingClient` no `BillingViewModel`
3. Validar compras no backend

## Testes

### Testes Unitários

```bash
./gradlew test
```

### Testes de Integração

```bash
./gradlew connectedAndroidTest
```

## Build Variants

O projeto suporta múltiplas variantes de build:

- **debug** - Desenvolvimento com logs
- **release** - Produção otimizada

## Troubleshooting

### Erro: "Gradle sync failed"
- Verificar versão do Gradle (8.2.0+)
- Limpar cache: `./gradlew clean`

### Erro: "Socket connection refused"
- Verificar se o App Companion está rodando no projetor
- Confirmar que ambos os dispositivos estão na mesma rede
- Verificar IP e porta corretos

### Erro: "Permission denied"
- Verificar permissões em `AndroidManifest.xml`
- Solicitar permissões em runtime (Android 6.0+)

## Documentação Adicional

- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)
- [Android Architecture Components](https://developer.android.com/topic/architecture)

## Contribuindo

Para contribuir com o projeto:

1. Criar uma branch para sua feature
2. Fazer commits descritivos
3. Abrir um Pull Request

## Licença

Este projeto é licenciado sob a licença MIT.

## Suporte

Para suporte, entre em contato através de:
- Email: support@manus.im
- Issues: GitHub Issues

---

**Desenvolvido com ❤️ por Manus**
