const { ApolloServer, gql } = require("apollo-server");
const mongoose = require("mongoose");
require("dotenv").config();

// Conexión a MongoDB
mongoose
  .connect(process.env.MONGODB_URI, { useNewUrlParser: true, useUnifiedTopology: true })
  .then(() => console.log("Conectado a MongoDB Atlas"))
  .catch((err) => console.error("Error al conectar a MongoDB:", err));

// Modelo de Persona
const PersonaSchema = new mongoose.Schema({
  nombre: { type: String, required: true },
  apellidos: { type: String, required: true },
  edad: { type: Number, required: true },
  pais: { type: String, required: true },
});

const Persona = mongoose.model("personas", PersonaSchema);

// Definir esquema de GraphQL
const typeDefs = gql`
  type Persona {
    id: ID!
    nombre: String!
    apellidos: String!
    edad: Int!
    pais: String!
  }

  type Query {
    personas: [Persona!]!
    persona(id: ID!): Persona
  }

  type Mutation {
    crearPersona(nombre: String!, apellidos: String!, edad: Int!, pais: String!): Persona!
    actualizarPersona(id: ID!, nombre: String, apellidos: String, edad: Int, pais: String): Persona
    eliminarPersona(id: ID!): String
  }
`;

// Definir resolvers de GraphQL
const resolvers = {
  Query: {
    personas: async () => await Persona.find(),
    persona: async (_, { id }) => await Persona.findById(id),
  },
  Mutation: {
    crearPersona: async (_, { nombre, apellidos, edad, pais }) => {
      const nuevaPersona = new Persona({ nombre, apellidos, edad, pais });
      return await nuevaPersona.save();
    },
    actualizarPersona: async (_, { id, nombre, apellidos, edad, pais }) => {
      const datosActualizados = { nombre, apellidos, edad, pais };
      return await Persona.findByIdAndUpdate(id, datosActualizados, { new: true });
    },
    eliminarPersona: async (_, { id }) => {
      await Persona.findByIdAndDelete(id);
      return "Persona eliminada correctamente";
    },
  },
};

// Crear servidor Apollo
const server = new ApolloServer({ typeDefs, resolvers });

// Iniciar servidor
server.listen({ port: process.env.PORT || 4000 }).then(({ url }) => {
  console.log(`Servidor GraphQL corriendo en ${url}`);
});
