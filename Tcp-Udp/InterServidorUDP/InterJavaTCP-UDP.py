import socket
import threading

# Función para manejar la conexión TCP y enviar los datos a un servidor UDP
def handle_client(tcp_socket):
    udp_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    udp_server_address = ('192.168.236.189', 12346)  # Dirección del servidor UDP

    try:
        while True:
            # Recibir datos del cliente TCP
            data = tcp_socket.recv(1024)
            if not data:
                break

            message = data.decode().strip()
            print(f"Mensaje recibido del cliente TCP: {message}")

            # Enviar los datos al servidor UDP
            udp_socket.sendto(data, udp_server_address)
            print(f"Mensaje enviado al servidor UDP: {message}")

    finally:
        print("Cliente TCP desconectado.")
        tcp_socket.close()

def main():
    # Crear socket TCP
    tcp_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    tcp_address = ('192.168.233.100', 12345)  # Dirección del puente TCP
    tcp_socket.bind(tcp_address)
    tcp_socket.listen(5)
    print(f"Puente TCP-UDP escuchando en {tcp_address}")

    while True:
        # Aceptar conexiones de clientes TCP
        client_socket, client_address = tcp_socket.accept()
        print(f"Cliente TCP conectado: {client_address}")

        # Crear hilo para manejar cada cliente
        client_thread = threading.Thread(target=handle_client, args=(client_socket,))
        client_thread.start()

if __name__ == "__main__":
    main()
