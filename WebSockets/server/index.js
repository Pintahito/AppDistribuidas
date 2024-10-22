import express from 'express';
import logger from 'morgan';
import { Server } from 'socket.io';
import { createServer } from 'node:http';
import cors from 'cors';  // Importa CORS para habilitarlo
import DBConnector from './dbconnector.js';

const port = process.env.PORT ?? 3000;

const app = express();
const server = createServer(app);

// Habilita CORS para el frontend (ajusta el origen según sea necesario)
app.use(cors({
    origin: 'http://localhost:3000',  // El puerto donde tu HTML está corriendo
    methods: ['GET', 'POST']
}));

const io = new Server(server, { connectionStateRecovery: {} });

// Crear la tabla de mensajes si no existe
async function initializeDatabase() {
    try {
        await DBConnector.query(`
            CREATE TABLE IF NOT EXISTS messages (
                id INT AUTO_INCREMENT PRIMARY KEY,
                content TEXT,
                user TEXT
            )
        `);
        console.log('Tabla messages asegurada.');
    } catch (e) {
        console.error('Error al crear la tabla:', e.message);
    }
}

initializeDatabase();

io.on('connection', async (socket) => {
    console.log('A user connected!');

    socket.on('disconnect', () => {
        console.log('A user has disconnected');
    });

    socket.on('chat message', async (data) => {
        const user = data.username ?? 'Anonimo';
        const msg = data.message;

        try {
            // Inserta el mensaje en la base de datos
            await DBConnector.query(`INSERT INTO messages (content, user) VALUES (?, ?)`, [msg, user]);
            console.log(`Message: ${msg} from: ${user}`);

            // Selecciona el último mensaje insertado en la base de datos
            const [lastMessage] = await DBConnector.query(`SELECT * FROM messages ORDER BY id DESC LIMIT 1`);

            // Emite el mensaje a todos los clientes
            io.emit('chat message', msg, lastMessage.id, user);
        } catch (e) {
            console.error('Error executing query:', e.message);
        }
    });

    if (!socket.recovered) {
        try {
            const results = await DBConnector.query(`SELECT id, content, user FROM messages WHERE id > ?`, [socket.handshake.auth.serverOffset]);
            results.forEach(result => {
                socket.emit('chat message', result.content, result.id, result.user);
            });
        } catch (e) {
            console.error('Error fetching messages:', e.message);
        }
    }
});

app.use(logger('dev'));

app.get('/', (req, res) => {
    res.sendFile(new URL('client/index.html', import.meta.url).pathname);
});

// Escucha en el puerto definido
server.listen(port, () => {
    console.log(`Server is running on port ${port}`);
});
