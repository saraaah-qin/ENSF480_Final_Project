import {
    BrowserRouter,
    Routes,
    Route,
  } from "react-router-dom";
import React from "react";
import Login from "./components/login";
import Dashboard from "./components/dashboard";

function App() {
    return (
        <BrowserRouter>
            <Routes>
            <Route path="/" element={<Login />} />
            <Route path="/dashboard" element={<Dashboard />} />
            </Routes>
        </BrowserRouter>
        );
  }
  
  export default App;

