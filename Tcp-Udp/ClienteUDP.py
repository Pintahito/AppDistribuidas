import socket
import threading

def receive_messages(client_socket):
    while True:
        try:
            # Recibir mensajes del servidor
            data, server = client_socket.recvfrom(1024)
            print(f'\nMensaje recibido del servidor: {data.decode()}')
        except:
            # Si hay un error (por ejemplo, el socket se cierra), se detiene el hilo
            print("Conexión cerrada por el servidor.")
            break

def main():
    # Creamos un socket UDP/IP
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

    # Dirección del servidor
    server_address = ('192.168.236.189', 12346)

    try:
        # Iniciamos un hilo para recibir mensajes del servidor
        receive_thread = threading.Thread(target=receive_messages, args=(client_socket,))
        receive_thread.daemon = True
        receive_thread.start()

        while True:
            # Solicitar mensaje al usuario
            message = input("Ingrese su mensaje (escriba 'exit' para salir): ")
            if message == 'exit':
                break

            # Enviamos datos al servidor
            client_socket.sendto(message.encode(), server_address)

    finally:
        # Cerrando el socket
        print('Cerrando el socket')
        client_socket.close()

if __name__ == "__main__":
    main()
