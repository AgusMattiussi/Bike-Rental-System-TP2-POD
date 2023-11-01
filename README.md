# POD TPE2 - GRUPO 6 - Alquiler de Bicicletas
Implementación de una aplicación de consola que utiliza el modelo de programación MapReduce junto con el framework HazelCast para el procesamiento de alquileres de bicicletas, basado en datos reales.

## Instrucciones de Compilación
Para compilar el proyecto, se deben ejecutar los siguientes comandos en la carpeta raíz del proyecto:
```bash
chmod +x compile.sh
./compile.sh
```
Este script se encargará de compilar el proyecto con `maven`, generando los archivos `.tar.gz` correspondientes en un directorio temporal `/tmp` en el cual se encontrarán los archivos disponibles para su ejecución.

## Instrucciones de Ejecución
### Servidor
Para ejecutar el servidor, se deben ejecutar los siguientes comandos en la carpeta raíz del proyecto:
```bash
cd ./tmp/tpe2-g6-server-2023.2Q/
./run-server
```

### Cliente de Consulta
Para ejecutar el cliente de consulta, se deben ejecutar los siguientes comandos en la carpeta raíz del proyecto:
```bash
cd ./tmp/tpe2-g6-client-2023.2Q/
./queryX -Daddresses='xx.xx.xx.xx:XXXX;yy.yy.yy.yy:YYYY' -DinPath=XX -DoutPath=YY [params]
````
donde:
- queryX es el script que corre la query X.
- -Daddresses refiere a las direcciones IP de los nodos con sus puertos (una o más, separadas por punto y coma)
- -DinPath indica el path donde están los archivos de entrada bikes.csv y stations.csv.
- -DoutPath indica el path donde estarán ambos archivos de salida.
- [params]: los parámetros extras que corresponden para algunas queries.
  - Para la query 2: -Dn=N (donde N es un número entero, indica límite de cantidad de resultados)
  - Para la query 4: -DstartDate=DD/MM/YYYY -DendDate=DD/MM/YYYY (donde DD/MM/YYYY es una fecha válida, indica el rango de fechas a considerar)


## Integrantes
| Nombre                 | Legajo |
|------------------------|--------|
| Ortu, Agustina Sol     | 61548  |
| Vasquez Currie, Malena | 60072  |
| Mattiussi, Agustín     | 61361  |
| Sasso, Julián Martín   | 61535  |