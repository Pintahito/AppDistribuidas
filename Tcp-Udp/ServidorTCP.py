import socket
import threading

clients = []

def handle_client(client_socket, client_address):
    print(f'Cliente conectado: {client_address}')
    while True:
        try:
            # Recibir mensaje del cliente
            data = client_socket.recv(1024)
            if not data:
                break

            print(f'Mensaje recibido de {client_address}: {data.decode()}')

            # Enviar el mensaje a todos los demás clientes
            broadcast_message(data, client_socket)
        except:
            break

    # Si el cliente se desconecta, lo eliminamos de la lista
    print(f'Cliente desconectado: {client_address}')
    client_socket.close()
    if client_socket in clients:
        clients.remove(client_socket)

def broadcast_message(message, sender_socket):
    # Enviar el mensaje a todos los clientes conectados excepto al remitente
    for client in clients:
        if client != sender_socket:
            try:
                client.sendall(message)
            except:
                client.close()
                if client in clients:
                    clients.remove(client)

def main():
    # Creamos un socket TCP/IP
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    # Enlace del socket a una dirección y puerto
    server_address = ('localhost', 12345)
    print(f'Iniciando en {server_address[0]} puerto {server_address[1]}')
    server_socket.bind(server_address)

    # Habilitamos el servidor para escuchar conexiones (máximo 5 clientes en cola)
    server_socket.listen(5)

    print('Esperando conexiones...')

    while True:
        # Esperamos la conexión de un cliente
        client_socket, client_address = server_socket.accept()

        # Añadimos al nuevo cliente a la lista de clientes
        clients.append(client_socket)

        # Creamos un hilo para manejar la conexión del cliente
        client_thread = threading.Thread(target=handle_client, args=(client_socket, client_address))
        client_thread.daemon = True
        client_thread.start()

if __name__ == "__main__":
    main()
