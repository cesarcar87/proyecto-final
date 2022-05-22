import React from "react";
import "./App.css";
import { Container, Row, Col } from "react-bootstrap";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import Navigation from "./Components/Navigation";
import Bienvenida from "./Components/Bienvenida";
import UserList from "./Components/User/UserList";
import Registro from "./Components/User/Registro";
import Login from "./Components/User/Login";
import Footer from "./Components/Footer";
import Home from "./Components/Home";

const App = () => {
  window.onbeforeunload = (event) => {
    const e = event || window.event;
    e.preventDefault();
    if (e) {
      e.returnValue = "";
    }
    return "";
  };

  return (
    <Router>
      <Navigation />
      <Container>
        <Row>
          <Col lg={12} className={"margin-top"}>
            <Routes>
              <Route path="/" exact component={Bienvenida} />
              <Route path="/home" exact component={Home} />
              <Route path="/users" exact component={UserList} />
              <Route path="/register" exact component={Registro} />
              <Route path="/login" exact component={Login} />
              <Route
                path="/logout"
                exact
                component={() => (
                  <Login message="User Logged Out Successfully." />
                )}
              />
            </Routes>
          </Col>
        </Row>
      </Container>
      <Footer />
    </Router>
  );
};

export default App;