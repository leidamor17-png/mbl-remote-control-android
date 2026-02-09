# MBLRemote Control Android - TODO

## Fase 1: Estrutura Base ✅

- [x] Criar estrutura de pastas (MVVM)
- [x] Configurar build.gradle.kts
- [x] Criar modelos de dados (Models.kt)
- [x] Implementar tema e cores (Jetpack Compose)
- [x] Criar componentes reutilizáveis (DirectionalPad, VolumeControl, ConnectionStatus)
- [x] Implementar SocketClient para comunicação
- [x] Implementar DataStore para armazenamento local
- [x] Criar ConnectionViewModel
- [x] Criar ConnectionRepository

## Fase 2: Telas Principais ✅

- [x] Implementar OnboardingScreen (4 slides)
- [x] Implementar ConnectionScreen (IP + Porta)
- [x] Implementar ControlScreen (D-Pad + Volume)
- [x] Implementar MainActivity com navegação
- [x] Integrar Jetpack Navigation

## Fase 3: Funcionalidades Premium (Em Progresso)

- [ ] Criar TouchpadScreen (mouse remoto)
- [ ] Implementar componente Touchpad com rastreamento de movimento
- [ ] Criar KeyboardScreen (teclado remoto)
- [ ] Implementar ShortcutsScreen (YouTube, Netflix, HDMI, etc.)
- [ ] Criar ModalUpgrade para upgrade Premium
- [ ] Implementar TabBar para alternar entre modos

## Fase 4: Google Play Billing

- [ ] Integrar Google Play Billing Library
- [ ] Criar BillingViewModel
- [ ] Implementar verificação de status Premium
- [ ] Criar fluxo de compra
- [ ] Implementar restauração de compras
- [ ] Validar compras no backend

## Fase 5: App Companion (Projetor)

- [ ] Criar novo módulo para App Companion
- [ ] Implementar Accessibility Service
- [ ] Criar ServerSocket para receber comandos
- [ ] Implementar executores de KeyEvent
- [ ] Implementar injetor de texto
- [ ] Implementar controlador de mouse
- [ ] Criar tela de configuração do Companion
- [ ] Implementar status de conexão

## Fase 6: Funcionalidades Avançadas

- [ ] Implementar reconexão automática
- [ ] Adicionar suporte a múltiplos dispositivos
- [ ] Implementar histórico de comandos
- [ ] Adicionar macros/atalhos customizados
- [ ] Implementar temas premium (Neon, Minimalista, Oceano)
- [ ] Adicionar suporte a idiomas múltiplos

## Fase 7: Testes e Qualidade

- [ ] Adicionar testes unitários (Mockk + JUnit)
- [ ] Adicionar testes de integração
- [ ] Implementar testes de UI (Compose Test)
- [ ] Configurar CI/CD (GitHub Actions)
- [ ] Adicionar code coverage
- [ ] Implementar lint e formatação de código

## Fase 8: Segurança e Otimização

- [ ] Implementar SSL/TLS para comunicação
- [ ] Adicionar validação de certificados
- [ ] Implementar autenticação (token)
- [ ] Otimizar performance (Profiler)
- [ ] Implementar crash reporting (Firebase Crashlytics)
- [ ] Adicionar analytics (Firebase Analytics)

## Fase 9: Documentação e Compilação

- [ ] Completar documentação técnica
- [ ] Criar guia de instalação do Companion
- [ ] Criar guia de troubleshooting
- [ ] Gerar APK de debug
- [ ] Gerar APK de release
- [ ] Gerar AAB para Play Store
- [ ] Preparar screenshots para Play Store
- [ ] Escrever descrição da app

## Fase 10: Publicação

- [ ] Criar conta de desenvolvedor no Google Play
- [ ] Configurar app no Play Console
- [ ] Fazer upload do AAB
- [ ] Configurar testes beta
- [ ] Publicar versão beta
- [ ] Coletar feedback
- [ ] Publicar versão de produção

## Notas Importantes

- Manter compatibilidade com React Native original
- Não usar APIs privadas
- Testar em múltiplos dispositivos (Android 8+)
- Validar permissões em runtime
- Implementar tratamento de erros robusto
- Documentar todas as mudanças

## Dependências Principais

- Jetpack Compose 1.6.0+
- Jetpack Navigation 2.7.7+
- Jetpack DataStore 1.0.0+
- Kotlin Coroutines 1.7.3+
- Google Play Billing 6.1.0+
- OkHttp 4.11.0+
- Gson 2.10.1+

## Próximas Prioridades

1. **Implementar Touchpad** - Funcionalidade Premium crítica
2. **Integrar Google Play Billing** - Monetização
3. **Criar App Companion** - Funcionalidade essencial
4. **Testes Unitários** - Qualidade de código
5. **Publicação** - Disponibilizar na Play Store

---

**Última atualização:** Fevereiro 2026
