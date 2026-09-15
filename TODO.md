🗺️ Planejamento Swingy - 42 School
Este documento rastreia os requisitos obrigatórios do PDF do projeto Swingy, mapeando o que já foi implementado e os próximos passos para a finalização.

🏗️ 1. Arquitetura e Configuração Base
[x] Gerenciador de Dependências: Configurar o projeto usando Maven (pom.xml).

[x] Padrão de Projeto: Estruturar o projeto seguindo rigorosamente o padrão MVC (Model-View-Controller).

[x] Máquina de Estados: Implementar o padrão State para gerenciar o fluxo do jogo (MenuState, HeroCreationState, MapState).

🦸‍♂️ 2. Modelos e Validação (Jakarta)
[x] Classe Hero: Criar atributos de Nome, Classe, Level, Experience, Attack, Defense e Hit Points.

[x] Padrão Builder: Utilizar o padrão Builder para instanciar heróis.

[x] Anotações de Validação: Aplicar o Jakarta/Hibernate Validator para garantir a integridade dos dados (ex: Nome não vazio, valores não negativos).

[x] Classes de Vilão: Criar as entidades dos monstros.

[x] Artefatos (Drops): Implementar os três tipos de artefatos:

[x] Weapon: Aumenta o Attack.

[x] Armor: Aumenta a Defense.

[x] Helm: Aumenta o Hit Points.

🖥️ 3. Interface de Usuário (Views)
[x] Multi-UI: Suportar execução em Console (texto) e GUI (janelas).

[x] Troca em Tempo Real: Permitir alternar entre GUI e Console a qualquer momento durante a execução.

[x] GUI - Renderização Non-blocking: Interface gráfica desenvolvida em Java Swing sem congelar a thread principal (LinkedBlockingQueue).

[x] GUI - Movimentação: Suporte a cliques nos botões e teclas nativas (WASD/Setas) via Key Bindings.

[x] GUI - Assets Gráficos: Tabuleiro gerado por camadas (Tile Layering) usando o padrão Flyweight (AssetManager) para otimização de memória.

🗺️ 4. Mapa e Movimentação
[x] Cálculo do Tamanho: Tamanho do mapa baseado na fórmula: (level - 1) * 5 + 10 - (level % 2).

[x] Posição Inicial: Herói sempre inicia no centro exato do mapa gerado.

[x] Controles: Sistema de locomoção em 4 direções (Norte, Sul, Leste, Oeste).

[x] Condição de Vitória (Mapa): Alcançar a borda salva o herói e retorna ao menu inicial.

[x] Geração de Vilões: Espalhar inimigos pelo mapa na geração do nível.

[x] Colisão: Detectar quando o herói pisa nas mesmas coordenadas de um vilão.

⚔️ 5. Sistema de Batalha (Próxima Fase)
[x] Estado de Encontro: Ao bater em um monstro, alternar para o estado de decisão (Lutar ou Fugir).

[x] Mecânica de Fuga:

50% de chance de escapar e voltar para a casa anterior.

Se falhar, o combate é forçado.

[x] Mecânica de Combate: Lógica de dano simulado (Ataque vs Defesa) até um dos lados chegar a 0 HP.

[x] Morte (Game Over): Se o HP do herói zerar, a missão falha e o jogo acaba.

[x] Vitória no Combate: O monstro some do mapa, o herói ganha XP, e tem uma chance de dropar um artefato.

[x] Sistema de Drops: Herói escolhe se equipa o artefato ou o ignora.

📈 6. Progressão (Leveling)
[x] Cálculo de XP: Implementar ganho de XP ao derrotar monstros.

[x] Fórmula de Level Up: Herói sobe de nível ao atingir a fórmula definida.

[x] Bônus de Level Up: Aumento de status ao subir de nível.

💾 7. Persistência de Dados (Save/Load)
[x] Banco de Dados / Arquivo: Properties implementado; PostgreSQL via Docker/JDBC como bônus.

[x] Salvar Progresso: Salvar automaticamente level, XP, stats e itens equipados.

[x] Menu "Carregar Herói": Listar e carregar heróis salvos.

📦 8. Entrega Final (Build)
[x] Plugin Assembly/Shade: Assembly configurado para gerar o Fat JAR.

[x] Teste de Execução: Fat JAR gerado com entry point configurado.

## Pendências restantes

[ ] Adicionar testes das transições completas do State Pattern e do BattleState.

[ ] Adicionar teste de integração JDBC usando PostgreSQL real.

[ ] Melhorar a GUI da seleção de heróis salvos com botões em vez de entrada numérica.

[ ] Decidir se o jogo terá progressão automática para novas fases ou encerrará no menu após a borda.