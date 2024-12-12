/* eslint-disable react/prop-types */
import React, { useState } from "react";
import ReactDOM from "react-dom/client";
import { ApolloClient, InMemoryCache, ApolloProvider, useQuery, useMutation, gql } from "@apollo/client";
import Modal from "react-modal";
import "./index.css";

// Configuración del cliente Apollo
const client = new ApolloClient({
  uri: "http://localhost:4000", // Cambia la URI si el backend tiene otra dirección
  cache: new InMemoryCache(),
});

// Consultas y Mutaciones
const GET_PERSONAS = gql`
  query GetPersonas {
    personas {
      id
      nombre
      apellidos
      edad
      pais
    }
  }
`;

const CREATE_PERSONA = gql`
  mutation CreatePersona($nombre: String!, $apellidos: String!, $edad: Int!, $pais: String!) {
    crearPersona(nombre: $nombre, apellidos: $apellidos, edad: $edad, pais: $pais) {
      id
      nombre
      apellidos
      edad
      pais
    }
  }
`;

const UPDATE_PERSONA = gql`
  mutation UpdatePersona($id: ID!, $nombre: String, $apellidos: String, $edad: Int, $pais: String) {
    actualizarPersona(id: $id, nombre: $nombre, apellidos: $apellidos, edad: $edad, pais: $pais) {
      id
      nombre
      apellidos
      edad
      pais
    }
  }
`;

const DELETE_PERSONA = gql`
  mutation DeletePersona($id: ID!) {
    eliminarPersona(id: $id)
  }
`;

// Componentes principales
// eslint-disable-next-line react/prop-types
const PersonasList = ({ onEdit }) => {
  const { loading, error, data } = useQuery(GET_PERSONAS);
  const [deletePersona] = useMutation(DELETE_PERSONA, {
    refetchQueries: [{ query: GET_PERSONAS }],
  });

  const handleDelete = (id) => {
    deletePersona({ variables: { id } });
  };

  if (loading) return <p className="loading">Cargando...</p>;
  if (error) return <p className="error">Error: {error.message}</p>;

  return (
    <div>
      <h2 className="title">Lista de Personas</h2>
      <ul className="person-list">
        {data.personas.map((persona) => (
          <li key={persona.id} className="person-item">
            <span className="person-details">{persona.nombre} {persona.apellidos} - {persona.edad} años, {persona.pais}</span>
            <div className="person-actions">
              <button className="btn btn-edit" onClick={() => onEdit(persona)}>Editar</button>
              <button className="btn btn-delete" onClick={() => handleDelete(persona.id)}>Eliminar</button>
            </div>
          </li>
        ))}
      </ul>
    </div>
  );
};

const PersonaForm = ({ isOpen, onRequestClose, initialValues }) => {
  const [createPersona] = useMutation(CREATE_PERSONA, {
    refetchQueries: [{ query: GET_PERSONAS }],
  });
  const [updatePersona] = useMutation(UPDATE_PERSONA, {
    refetchQueries: [{ query: GET_PERSONAS }],
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    const form = e.target;
    const formData = new FormData(form);
    const variables = Object.fromEntries(formData.entries());
    variables.edad = parseInt(variables.edad, 10); // Convertir edad a número

    if (initialValues.id) {
      updatePersona({ variables: { ...variables, id: initialValues.id } });
    } else {
      createPersona({ variables });
    }

    form.reset();
    onRequestClose();
  };

  return (
    <Modal isOpen={isOpen} onRequestClose={onRequestClose} className="modal" overlayClassName="overlay">
      <form onSubmit={handleSubmit} className="persona-form">
        <h2 className="title">{initialValues.id ? "Editar Persona" : "Agregar Persona"}</h2>
        <input name="nombre" placeholder="Nombre" defaultValue={initialValues.nombre} required className="input"/>
        <input name="apellidos" placeholder="Apellidos" defaultValue={initialValues.apellidos} required className="input"/>
        <input name="edad" type="number" placeholder="Edad" defaultValue={initialValues.edad} required className="input"/>
        <input name="pais" placeholder="País" defaultValue={initialValues.pais} required className="input"/>
        <button type="submit" className="btn btn-submit">{initialValues.id ? "Guardar Cambios" : "Agregar"}</button>
      </form>
    </Modal>
  );
};

const App = () => {
  const [modalIsOpen, setModalIsOpen] = useState(false);
  const [currentPersona, setCurrentPersona] = useState({});

  const openModal = (persona = {}) => {
    setCurrentPersona(persona);
    setModalIsOpen(true);
  };

  const closeModal = () => {
    setModalIsOpen(false);
    setCurrentPersona({});
  };

  return (
    <ApolloProvider client={client}>
      <div className="app-container">
        <h1 className="main-title">Gestión de Personas</h1>
        <button className="btn btn-add" onClick={() => openModal()}>Agregar Persona</button>
        <PersonasList onEdit={openModal} />
        <PersonaForm isOpen={modalIsOpen} onRequestClose={closeModal} initialValues={currentPersona} onSubmit={closeModal} />
      </div>
    </ApolloProvider>
  );
};

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);

export default App;
