# 📱 Aplicativo Android de Gerenciamento de Pessoas com Firebase Firestore
Um aplicativo Android simples para gerenciar informações de pessoas, com operações de CRUD integradas ao Firebase Cloud Firestore e sincronização em tempo real.
## 🚀 Funcionalidades
- 🔄 Sincronização em tempo real com Firestore
- ➕ Adição de pessoas com nome, e-mail, telefone, endereço e idade
- ✏️ Edição e exclusão com confirmação
- 📴 Suporte offline automático
- 🔍 Consultas estruturadas e escaláveis
- 🎨 UI moderna com Material Design
## 🧱 Estrutura do Projeto
```bash
📁 app/
├── java/com/example/myapplication/
│   ├── MainActivity.java
│   ├── PessoaAdapter.java
│   └── model/Pessoa.java
└── res/layout/
    ├── activity_main.xml
    ├── item_pessoa.xml
    └── dialog_pessoa.xml
```
## 📱 Telas principais
- Lista de pessoas com opções de editar/excluir
- Diálogo para adicionar novas pessoas
- Diálogo para confirmar exclusão

## ⚙️ Configuração rápida
1. Clone o repositório
2. Adicione seu `google-services.json` na pasta app/
3. Sincronize o Gradle e execute

## 🔧 Tecnologias
- Java
- Android SDK
- Firebase Firestore
- RecyclerView
- Material Design

---
Desenvolvido por Lucas Giovanella e Renan Wietholter
