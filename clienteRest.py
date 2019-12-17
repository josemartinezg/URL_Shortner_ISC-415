import requests
import json

class URL:
       def __init__(self, urlGenerada, urlReferencia, fechaCreacion, cantAccesos=0):
        self.urlGenerada = urlGenerada
        self.urlReferencia = urlReferencia
        self.fechaCreacion = fechaCreacion
        self.cantAccesos = cantAccesos

    def __str__(self):
        url = "Url Generada: " + str(self.urlGenerada) + "\Url de Referencia: " + str(
            self.urlReferencia) + "\Fecha Creacion: " + str(self.fechaCreacion) + "\Cantidad de accesos: " + str(self.cantAccesos)
        return url


def jprint(obj):
    # create a formatted string of the Python JSON object
    text = json.dumps(obj, sort_keys=True, indent=4)
    print(text)


def print_menu():
    print()
    print("-------------------- REST Api " + "  ----------------------")
    print("| Eliga la opción deseada en las siguientes opciones: |")
    print("| 1. Listado de URLs                                  |")
    print("| 2. Estadisticas por Visita                          |")
    print("| 3. Acortar URL                                      |")
    print("| 0. Exit                                             |")
    print("-------------------------------------------------------")


def get_urls_usuario(username):
    req = requests.get('http://localhost:4567/api/urls/' + username)
    jprint(req.json())
    print()

def get_accesos_usuario(username, id_url):
    req = requests.get('http://localhost:4567/api/urls/' + username + '/' + id_url)
    jprint(req.json())
    print()


def post_estudiante():
    urlReferencia = input("\nInserte URL de Referencia: ")
    username = input("Inserte el nombre de usuario: ")
    url = URL(urlReferencia, username)
    try:
        requests.post('http://localhost:4567/rest/estudiantes/',
                      json=url.__dict__)
        print("Se ha creado exitosamente. ")
        print()
    except:
        print("La creación de estudiante no funciono. ")


def get_estudiante():
    urlReferencia = input("Inserte URL de Referencia: ")
    username = input("Inserte el nombre de usuario: ")
    response = requests.get(
        "http://localhost:4567/rest/estudiantes/" + str(matricula))
    estudiante = Estudiante(
        response.json()["nombre"], response.json()["correo"], response.json()["carrera"], response.json()["matricula"])
    print("\n" + str(estudiante))
    return estudiante


opcion = -1
while True:
    print_menu()
    opcion = int(input("Opción tomada: "))

    if opcion == 0:
        break
    elif opcion == 1:
        username = input("Inserte el nombre del usuario: ")
        get_urls_usuario(username)
    elif opcion == 2:
        username = input("Inserte el nombre del usuario: ")
        id_url = input("Indique el ID del vinculo cuyas estadísticas quiere vizualizar: ")
        get_accesos_usuario(username, id_url)
    elif opcion == 3:
        post_estudiante()