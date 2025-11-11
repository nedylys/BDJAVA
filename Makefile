# Dossiers sources
SRC_DIRS := main tests
BIN_DIR := bin
JAR := ojdbc6.jar

# Tous les fichiers .java
SRC_FILES := $(wildcard main/*.java) $(wildcard tests/*.java)

# Règle par défaut
all: $(BIN_DIR) classes

# Créer le dossier bin si nécessaire
$(BIN_DIR):
	mkdir -p $(BIN_DIR)

# Compiler toutes les classes
classes: $(BIN_DIR)
	javac -d $(BIN_DIR) -cp $(JAR) $(SRC_FILES)

# Lancer l'application principale (main_query.java dans tests/)
run: all
	java -cp $(BIN_DIR):$(JAR) main_query

# Lancer les tests (si tu as d'autres classes de tests)
test: all
	java -cp $(BIN_DIR):$(JAR) tests.main_query

# Nettoyer le dossier bin
clean:
	rm -rf $(BIN_DIR)
