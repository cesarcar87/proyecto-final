import React from "react";
import { useSelector } from "react-redux";
import autToken from "../Utils/AutToken";
import { Alert } from "react-bootstrap";

const Home = () => {
  if (localStorage.jwtToken) {
    autToken(localStorage.jwtToken);
  }

  const auth = useSelector((state) => state.auth);

  return (
    <Alert style={{ backgroundColor: "#343A40", color: "#ffffff80" }}>
      Welcome {auth.username}
    </Alert>
  );
};

export default Home;