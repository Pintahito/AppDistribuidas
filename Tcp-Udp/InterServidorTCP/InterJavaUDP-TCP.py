import socket
import threading

# Función para manejar la conexión UDP y retransmitir los datos a un servidor TCP
def handle_udp_to_tcp(udp_socket):
    tcp_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    tcp_server_address = ('192.168.236.189', 12345)  # Dirección del servidor TCP
    tcp_socket.connect(tcp_server_address)

    try:
        while True:
            # Recibir datos del cliente UDP
            data, addr = udp_socket.recvfrom(1024)
            if not data:
                break

            message = data.decode().strip()
            print(f"Mensaje recibido del cliente UDP: {message}")

            # Enviar los datos al servidor TCP
            tcp_socket.sendall(data)
            print(f"Mensaje enviado al servidor TCP: {message}")

    except Exception as e:
        print(f'Error: {e}')
    finally:
        tcp_socket.close()

def main():
    # Crear socket UDP
    udp_socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    udp_address = ('192.168.233.100', 12346)  # Dirección del puente UDP
    udp_socket.bind(udp_address)

    print(f"Puente UDP-TCP escuchando en {udp_address}")

    while True:
        # Manejar los datos entrantes desde UDP y retransmitirlos a TCP
        handle_udp_to_tcp(udp_socket)

if __name__ == "__main__":
    main()
