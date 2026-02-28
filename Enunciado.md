# Resumen ordenado: Working Directory, compilación y ejecución en Java

## 1) Idea clave (regla de oro)

El **Working Directory (WD)** es **la carpeta donde está la terminal cuando ejecutas el comando** (`java`, `mvn`, etc.).

- El WD **no depende** de dónde está el archivo `.java`.
- El programa buscará rutas relativas (`data/SecretFile.txt`, `./algo`, `../algo`) desde ese WD.

---

## 2) Estructura real de este proyecto

```text
laboratorio-rutas/
├─ pom.xml
├─ data/
│  └─ SecretFile.txt
├─ src/
│  └─ main/
│     └─ java/
│        └─ lab/
│           └─ rutas/
│              └─ Buscador.java   (package lab.rutas;)
└─ out/                            (la crearemos para .class manuales)
```

`Buscador.java` está en paquete `lab.rutas`, por eso su clase se ejecuta como:

```bash
lab.rutas.Buscador
```

---

## 3) Tres conceptos que no hay que mezclar

1. **Lugar del código fuente**: dónde vive `Buscador.java`.
2. **Lugar de compilación**: carpeta donde guardas `.class` (ej. `out/`).
3. **Lugar de ejecución (WD)**: carpeta actual de la terminal al ejecutar `java`.

Además:

- **Classpath (`-cp`)**: ruta donde Java busca clases compiladas (`.class`).
- **Argumentos del programa**: rutas de archivos que `main(String[] args)` recibe y que se resuelven desde el WD.

---

## 4) Flujo manual recomendado (sin Maven), desde la raíz

Ubícate en la raíz del proyecto (`laboratorio-rutas/`).

### Compilar a `out/` (sin ensuciar `src/`)

```bash
javac -d out src/main/java/lab/rutas/Buscador.java
```

Resultado esperado:

```text
out/
└─ lab/
   └─ rutas/
      └─ Buscador.class
```

### Ejecutar desde la raíz

```bash
java -cp out lab.rutas.Buscador data/SecretFile.txt
```

Aquí:

- `-cp out` → Java encuentra `lab/rutas/Buscador.class`.
- `data/SecretFile.txt` → se busca desde el WD (raíz), por eso funciona directo.

---

## 5) Mapa mental rápido de rutas relativas

Si estás en...

- **raíz**: `data/SecretFile.txt`
- **src/main**: `../../data/SecretFile.txt`
- **data**: `SecretFile.txt`

Y para `out` (classpath):

- **raíz**: `out`
- **src/main**: `../../out`
- **data**: `../out`

---

## 6) Práctica: Misiones A, B y C

> Objetivo: ejecutar la **misma** clase (`lab.rutas.Buscador`) desde distintas carpetas, ajustando `-cp` y la ruta del archivo.

### Misión A — “Estándar” (desde la raíz)

1. Ir a la raíz del proyecto.
2. Compilar a `out/`.
3. Ejecutar buscando `data/SecretFile.txt`.

```bash
javac -d out src/main/java/lab/rutas/Buscador.java
java -cp out lab.rutas.Buscador data/SecretFile.txt
```

Esperado en salida:

- `Working Directory` termina en `.../laboratorio-rutas`
- `RESULTADO: Archivo encontrado!`

---

### Misión B — “Infiltrado” (desde `src/main`)

Sin recompilar, mueve la terminal a `src/main`:

```bash
cd src/main
java -cp ../../out lab.rutas.Buscador ../../data/SecretFile.txt
```

Explicación:

- `../../out` porque `out` está dos niveles arriba de `src/main`.
- `../../data/SecretFile.txt` por la misma razón.

Esperado:

- `Working Directory` termina en `.../laboratorio-rutas/src/main`
- `RESULTADO: Archivo encontrado!`

---

### Misión C — “Local” (desde `data`)

Mueve la terminal a `data`:

```bash
cd data
java -cp ../out lab.rutas.Buscador SecretFile.txt
```

Explicación:

- `../out` sube a raíz y entra a `out` para encontrar clases.
- `SecretFile.txt` está en el WD actual (`data`).

Esperado:

- `Working Directory` termina en `.../laboratorio-rutas/data`
- `RESULTADO: Archivo encontrado!`

---

## 7) Limpieza y recompilación rápida (opcional)

Si quieres reiniciar `out` y probar todo otra vez:

```bash
# PowerShell
Remove-Item -Recurse -Force out -ErrorAction SilentlyContinue
javac -d out src/main/java/lab/rutas/Buscador.java
java -cp out lab.rutas.Buscador data/SecretFile.txt
```

---

## 8) Relación con Maven (contexto)

En Maven normalmente no haces esto manualmente porque:

- compila en `target/classes`
- ejecuta con classpath configurado automáticamente

Pero esta práctica manual es perfecta para entender:

- WD
- classpath
- rutas relativas
- diferencia entre compilar y ejecutar

---

## 10) Siguiente Nivel: El archivo .jar (Java Archive)

En la industria, no entregas una carpeta llena de archivos `.class` sueltos. Entregas un único archivo comprimido llamado **JAR**. Es como un `.zip` pero con esteroides que Java sabe ejecutar.

### Paso 1: Crear el JAR (Empaquetar)

Una vez que ya has compilado (tienes la carpeta `out`), ejecutas esto:

```bash
jar -cvfe buscador.jar lab.rutas.Buscador -C out .
```

**¿Qué significa esto?**

- `-c`: **C**reate (Crear).
- `-v`: **V**erbose (Cuéntame todo lo que haces).
- `-f`: **F**ile (El nombre del archivo será `buscador.jar`).
- `-e`: **E**ntry-point (Le dices cuál es la clase que tiene el main: `lab.rutas.Buscador`).
- `-C out .`: "Vete a la carpeta `out` y mete todo lo que hay dentro".

### Paso 2: Ejecutar el JAR

Ahora ya no necesitas el `-cp` ni el nombre de la clase, porque todo eso ya está "dentro" del paquete.

```bash
java -jar buscador.jar data/SecretFile.txt
```

---

## 11) Plantilla genérica (para cualquier proyecto)

Si cambian nombres de carpetas, aplica siempre el mismo patrón:

1. Compila a una carpeta de salida (`out` o `target/classes`).
2. Ejecuta con `java -cp <salida> <paquete.ClasePrincipal> <ruta-al-archivo>`.
3. Ajusta `<salida>` y `<ruta-al-archivo>` según **dónde está tu terminal**.

Esa es la mecánica universal.
