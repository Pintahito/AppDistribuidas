import socket
import threading

def receive_messages(client_socket):
    while True:
        try:
            # Recibir mensajes del servidor
            data = client_socket.recv(1024)
            if not data:
                # Si no recibimos datos, significa que la conexión se cerró
                print("Conexión cerrada por el servidor.")
                break
            print(f'\nMensaje recibido del servidor: {data.decode()}')
        except:
            print("Error al recibir mensajes.")
            break

def main():
    # Creamos un socket TCP/IP
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    # Conectamos el socket al servidor
    server_address = ('192.168.236.189', 12345)
    print(f'Conectando a {server_address[0]} puerto {server_address[1]}')
    client_socket.connect(server_address)

    try:
        # Iniciar un hilo para recibir mensajes del servidor
        receive_thread = threading.Thread(target=receive_messages, args=(client_socket,))
        receive_thread.daemon = True
        receive_thread.start()

        while True:
            # Solicitar mensaje al usuario
            message = input("Ingrese su mensaje (escriba 'exit' para salir): ")
            if message.lower() == 'exit':
                break

            # Enviar mensaje al servidor
            client_socket.sendall(message.encode())

    finally:
        # Cerrando el socket
        print('Cerrando el socket')
        client_socket.close()

if __name__ == "__main__":
    main()
