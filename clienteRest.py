import requests
import json

class URL:
    def __init__(self, urlReferencia):
        self.urlReferencia = urlReferencia
    def __str__(self):
            url = "\nUrl de Referencia: " + str(self.urlReferencia) 
            return url

def jprint(obj):
    # create a formatted string of the Python JSON object
    text = json.dumps(obj, sort_keys=True, indent=4)
    print(text)


def print_menu():
    print()
    print("-------------------- REST Api " + "  ----------------------")
    print("| Eliga la opción deseada en las siguientes opciones: |")
    print("| 1. Generar Token                                    |")
    print("| 2. Listado de URLs                                  |")
    print("| 3. Estadisticas por Visita                          |")
    print("| 4. Acortar URL                                      |")
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


def post_estudiante(username):
    urlReferencia = input("\nInserte URL de Referencia: ")
    url = URL(urlReferencia)
    try:
        r = requests.post('http://localhost:4567/api/generarURL/' + username,
                      json=url.__dict__)
        print(r.text)
        print("Se ha creado exitosamente. ")
        print()
    except:
        print("La creación de estudiante no funciono. ")

opcion = -1
TOKEN = ''
headers = {}
while True:
    print_menu()
    opcion = int(input("Opción tomada: "))

    if opcion == 0:
        break
    if opcion == 1:
        TOKEN = requests.post('http://localhost:4567/token').json()['token']
        headers['token'] = TOKEN
        print("Su nuevo token: {0}".format(TOKEN))

    elif opcion == 2:
        username = input("Inserte el nombre del usuario: ")
        if (username == ""):
            username = "guess"
        get_urls_usuario(username)
    elif opcion == 3:
        username = input("Inserte el nombre del usuario: ")
        if (username == ""):
            username = "guess"
        id_url = input("Indique el ID del vinculo cuyas estadísticas quiere vizualizar: ")
        get_accesos_usuario(username, id_url)
    elif opcion == 4:
        username = input("Inserte el nombre del usuario: ")
        if (username == ""):
            username = "guess"
    
        post_estudiante(username)