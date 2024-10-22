import mysql from 'mysql2/promise'; // Importa desde mysql2 con soporte de promesas
import dotenv from 'dotenv';

dotenv.config({ path: 'server/.env' });

const config = {
    host: process.env.DB_HOST,
    user: process.env.DB_USER,
    password: process.env.DB_PASSWORD,
    database: process.env.DB_DATABASE,
};

class DBConnector {
    constructor() {
        this.dbconnector = mysql.createPool(config);
    }

    async query(params) {
        try {
            const [results] = await this.dbconnector.query(params);
            return results;
        } catch (error) {
            throw error;
        }
    }
}

export default new DBConnector();
