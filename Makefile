MVN := mvn

ifeq ($(OS),Windows_NT)
DB_ENV := set SWINGY_PERSISTENCE=db&&
else
DB_ENV := SWINGY_PERSISTENCE=db
endif

.PHONY: help clean compile test verify package install dependency-tree run db-up db-down run-db

help:
	@echo "Comandos disponíveis:"
	@echo "	make clean            Limpa os arquivos compilados"
	@echo "	make compile          Compila o projeto"
	@echo "	make test            Executa os testes"
	@echo "	make verify          Compila e executa todas as verificações"
	@echo "	make package         Gera o arquivo JAR"
	@echo "	make install         Instala o JAR no repositorio local"
	@echo "	make dependency-tree  Mostra as dependencias"
	@echo "\tmake db-up            Inicia o PostgreSQL via Docker"
	@echo "\tmake db-down          Para o PostgreSQL via Docker"
	@echo "\tmake run-db           Executa usando persistencia PostgreSQL"

run: package
ifeq ($(filter console gui,$(MODE)),)
	@echo "Uso: make run MODE=console"
	@echo "  ou: make run MODE=gui"
	@exit 1
else
	@echo "Executando no modo $(MODE)..."
	java -jar target/swingy-1.0-SNAPSHOT-jar-with-dependencies.jar $(MODE)
endif

clean:
	$(MVN) clean

compile:
	$(MVN) compile

test:
	$(MVN) clean test

verify:
	$(MVN) clean verify

package:
	$(MVN) clean package

install:
	$(MVN) clean install

dependency-tree:
	$(MVN) dependency:tree

db-up:
	docker compose up -d

db-down:
	docker compose down

run-db: package db-up
	$(DB_ENV) java -jar target/swingy-1.0-SNAPSHOT-jar-with-dependencies.jar $(if $(MODE),$(MODE),console)