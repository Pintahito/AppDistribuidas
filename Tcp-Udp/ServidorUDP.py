import socket
import threading

clients = []

def handle_client(server_socket, client_address):
    while True:
        try:
            data, _ = server_socket.recvfrom(1024)
            if not data:
                break
            print(f'Mensaje recibido del cliente {client_address[0]}:{client_address[1]}: {data.decode()}')
            response = f"Mensaje recibido por el servidor: {data.decode()}"
            
            # Enviar mensaje a todos los clientes conectados
            for client in clients:
                if client != client_address:
                    server_socket.sendto(response.encode(), client)
        except ConnectionResetError:
            break

    print(f'Cliente {client_address} desconectado')
    if client_address in clients:
        clients.remove(client_address)

def main():
    # Creamos un socket UDP/IP
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

    # Enlace del socket a una dirección y puerto
    server_address = ('localhost', 12346)
    print(f'Iniciando en {server_address[0]} puerto {server_address[1]}')
    server_socket.bind(server_address)

    print('Esperando mensajes entrantes...')

    while True:
        # Esperando mensajes de clientes
        data, client_address = server_socket.recvfrom(1024)

        if client_address not in clients:
            print(f'Nuevo cliente conectado: {client_address}')
            clients.append(client_address)
            # Iniciamos un hilo para manejar la conexión con el cliente
            client_thread = threading.Thread(target=handle_client, args=(server_socket, client_address))
            client_thread.daemon = True
            client_thread.start()

if __name__ == "__main__":
    main()
