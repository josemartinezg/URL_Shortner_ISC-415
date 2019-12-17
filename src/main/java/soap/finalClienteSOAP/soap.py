from suds.client import Client
import json
import emoji


url = "http://localhost:7777/ws/UrlWebService?wsdl"
client = Client(url)
service = client.service

# Suds ( https://github.com/cackharot/suds-py3 )  version: 1.3.4.0 IN  build: 20191029

# Service ( UrlWebServiceService ) tns="http://soap/"
#    Prefixes (1)
#       ns0 = "http://soap/"
#    Ports (1):
#       (UrlWebServicePort)
#          Methods (2):
#             generarURL(xs:string arg0, )
#             getUrls(xs:string arg0, )
#          Types (10):
#             acceso
#             generarURL
#             generarURLResponse
#             getUrls
#             getUrlsResponse
#             navegador
#             sistemaOperativo
#             timestamp
#             url
#             usuario


def jprint(obj):
    parsed = json.loads(obj)
    # create a formatted string of the Python JSON object
    text = json.dumps(parsed, sort_keys=True, indent=4)
    print(text)


def print_menu():
    print()
    print("-------------------- SOAP Api " +
          emoji.emojize(":grinning_face_with_big_eyes:") + "  ----------------------")
    print("| Eliga la opción deseada en las siguientes opciones: |")
    print("| 1. Generar URL acortado                             |")
    print("| 2. Lista de URLs por Usuario                        |")
    print("| 0. Exit                                             |")
    print("-------------------------------------------------------")


def get_urls_usuario():
    usuario = input("Ingrese el nombre del usuario: ")
    req = service.getUrls(usuario)
    for url in req:
        jprint(url)


def get_url_generada():
    usuario = input("Ingrese el nombre del usuario: ")
    url_referencia = input("Ingrese la Url que desea acortar: ")
    req = service.generarURL(str(url_referencia), usuario)
    jprint(req)


opcion = -1
while True:
    print_menu()
    opcion = int(input("Opción tomada: "))

    if opcion == 0:
        break
    elif opcion == 1:
        get_url_generada()
    elif opcion == 2:
        get_urls_usuario()
