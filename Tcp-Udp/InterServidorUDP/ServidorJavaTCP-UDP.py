# import socket

# def main():
#     # Crear socket UDP
#     udp_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
#     udp_address = ('localhost', 12346)
#     udp_socket.bind(udp_address)

#     print(f"Servidor UDP iniciado en {udp_address}")

#     while True:
#         # Recibir mensajes
#         data, client_address = udp_socket.recvfrom(1024)
#         message = data.decode().strip()
#         print(f"Mensaje recibido del puente: {message}")

#         # Enviar una respuesta si es necesario
#         response = f"Mensaje recibido por el servidor UDP: {message}"
#         udp_socket.sendto(response.encode(), client_address)

# if __name__ == "__main__":
#     main()

import socket
import mysql.connector
from mysql.connector import Error

def store_message_in_db(message):
    try:
        # Conectar a la base de datos
        connection = mysql.connector.connect(
            host='localhost',
            database='sockets',
            user='root',
            password='1022'
        )
        if connection.is_connected():
            cursor = connection.cursor()
            # Insertar el mensaje en la base de datos
            insert_query = "INSERT INTO mensajes (mensaje) VALUES (%s)"
            cursor.execute(insert_query, (message,))
            connection.commit()
            print("Mensaje almacenado en la base de datos")

    except Error as e:
        print(f"Error al conectar a MySQL: {e}")
    finally:
        if connection.is_connected():
            cursor.close()
            connection.close()

def main():
    # Crear socket UDP
    udp_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    udp_address = ('192.168.214.255', 12346)
    udp_socket.bind(udp_address)

    print(f"Servidor UDP iniciado en {udp_address}")

    while True:
        # Recibir mensajes
        data, client_address = udp_socket.recvfrom(1024)
        message = data.decode().strip()
        print(f"Mensaje recibido del puente: {message}")

        # Almacenar el mensaje en la base de datos
        store_message_in_db(message)

        # Enviar una respuesta si es necesario
        response = f"Mensaje recibido por el servidor UDP: {message}"
        udp_socket.sendto(response.encode(), client_address)

if __name__ == "__main__":
    main()

