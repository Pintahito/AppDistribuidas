import socket
import threading

# Función para manejar el cliente TCP
def handle_client(client_socket):
    print("Cliente TCP conectado")
    while True:
        try:
            # Recibir mensaje del cliente
            data = client_socket.recv(1024)
            if not data:
                break

            message = data.decode().strip()
            print(f"Mensaje recibido del puente: {message}")

            # Responder al cliente TCP (si es necesario)
            response = f"Mensaje recibido por el servidor TCP: {message}"
            client_socket.sendall(response.encode())
        except:
            break

    client_socket.close()
    print("Cliente TCP desconectado")

def main():
    # Crear socket TCP
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_address = ('localhost', 12345)
    server_socket.bind(server_address)
    server_socket.listen(5)
    print(f"Servidor TCP escuchando en {server_address}")

    while True:
        # Aceptar conexiones entrantes
        client_socket, client_address = server_socket.accept()

        # Crear hilo para manejar al cliente TCP
        client_thread = threading.Thread(target=handle_client, args=(client_socket,))
        client_thread.start()

if __name__ == "__main__":
    main()
