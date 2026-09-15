# Swingy - Status do Projeto

Atualizado em 2026-09-15.

## Estado atual

### Implementado

- Maven configurado com Java 25.
- Estrutura MVC.
- State Pattern com menu, criacao de heroi e mapa.
- Console e GUI Swing.
- Troca entre console e GUI.
- Mapa com tamanho baseado no nivel.
- Hero iniciado no centro do mapa.
- Movimentacao em quatro direcoes.
- Mapa visual com assets e atualizacao incremental da GUI.
- Validacao Jakarta/Hibernate Validator.
- Hero com Builder.
- Artifact, ArtifactFactory e ArtifactType.
- Villain basico.
- BattleController com luta, fuga, XP e drops, ainda sem integracao ao fluxo.
- Leveling inicial do Hero.
- Fat JAR via Maven Assembly.
- Testes atuais passando: 11 testes, 0 falhas.

## Pendencias reais

### Alta prioridade

1. Integrar o fat JAR ao Makefile. Concluido nesta etapa: `make run MODE=console|gui` agora usa `swingy-1.0-SNAPSHOT-jar-with-dependencies.jar`.
2. Criar e distribuir vilões no mapa. Concluido: o `MapController` gera vilões em celulas internas unicas e o mapa os renderiza como `V`.
3. Detectar colisao entre Hero e Villain. Concluido: o movimento expõe o vilão encontrado para o proximo estado.
4. Integrar o BattleController ao State Pattern com um `BattleState`. Concluido: lutar, fugir, remover vilão derrotado e encerrar em derrota.

### Media prioridade

5. Implementar retorno para a posicao anterior quando a fuga funcionar. Concluido.
6. Remover o vilão derrotado do mapa. Concluido.
7. Implementar Game Over quando o Hero morrer. Concluido no fluxo de batalha.
8. Ao alcançar a borda, salvar o herói e retornar ao menu inicial. Concluido. A progressao automática de fase foi removida conforme o fluxo desejado.
9. Implementar persistencia para salvar e carregar herois, XP, atributos e artefatos equipados. Concluido com arquivo Properties em `~/.swingy/hero.properties`.

### Testes e qualidade

10. Adicionar testes para MapController, movimentacao, centro, borda e colisao. Parcial: geração e colisao cobertas.
11. Adicionar testes para BattleController, fuga, dano, derrota, vitoria, XP e drops.
12. Adicionar testes para as transicoes do State Pattern e para o BattleState.
13. Corrigir `ArtifactTest`: concluido.
14. Atualizar o TODO.md para refletir as implementacoes que ja existem.

## Proximo passo de desenvolvimento

O proximo recurso de gameplay recomendado e criar o `BattleState`, usando o vilão exposto pelo `MapController` para conectar o `BattleController` ja existente ao fluxo principal.

## Comandos principais

```bash
mvn test
mvn clean package
make run MODE=console
make run MODE=gui
```

No Windows com MinGW, use `mingw32-make` no lugar de `make` se necessario.
