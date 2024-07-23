import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Box, Button, FormControl, FormLabel, Input, Stack, Text, Heading } from "@chakra-ui/react";
import Swal from "sweetalert2";
import axios from "axios";

function SignUp() {
  const [userDetails, setUserDetails] = useState({
    username: "",
    email: "",
    address: "",
    password: "",
  });
  const navigate = useNavigate();

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setUserDetails({
      ...userDetails,
      [name]: value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!userDetails.username || !userDetails.email || !userDetails.password) {
      Swal.fire({
        title: 'Error',
        text: 'Please fill in all required fields.',
        icon: 'error',
        confirmButtonText: 'OK'
      });
      return;
    }

    try {
      const response = await axios.post("http://localhost:8080/api/user/register", userDetails);

      if (response.status === 201 || response.status === 200) {
        Swal.fire({
          title: 'User registered',
          text: 'You can now log in.',
          icon: 'success',
          confirmButtonText: 'OK'
        });
        navigate("/login");
      }
    } catch (error) {
      Swal.fire({
        title: 'Error',
        text: 'An error occurred while creating your account.',
        icon: 'error',
        confirmButtonText: 'OK'
      });
    }
  };

  return (
    <Box bg="gray.50" minH="100vh" display="flex" alignItems="center" justifyContent="center" p={6}>
      <Box bg="white" p={8} borderRadius="lg" shadow="md" maxW="md" w="full">
        <Heading as="h1" size="lg" mb={4} textAlign="center">
          Sign Up
        </Heading>
        <form onSubmit={handleSubmit}>
          <Stack spacing={4}>
            <FormControl id="username" isRequired>
              <FormLabel>Username</FormLabel>
              <Input
                type="text"
                name="username"
                value={userDetails.username}
                onChange={handleInputChange}
                placeholder="Enter username"
              />
            </FormControl>
            <FormControl id="email" isRequired>
              <FormLabel>Email Address</FormLabel>
              <Input
                type="email"
                name="email"
                value={userDetails.email}
                onChange={handleInputChange}
                placeholder="abc@gmail.com"
              />
            </FormControl>
            <FormControl id="address">
              <FormLabel>Address</FormLabel>
              <Input
                type="text"
                name="address"
                value={userDetails.address}
                onChange={handleInputChange}
                placeholder="Address"
              />
            </FormControl>
            <FormControl id="password" isRequired>
              <FormLabel>Password</FormLabel>
              <Input
                type="password"
                name="password"
                value={userDetails.password}
                onChange={handleInputChange}
                placeholder="••••••••"
              />
            </FormControl>
            <Stack spacing={3}>
              <Button colorScheme="blue" type="submit">
                Sign Up
              </Button>
              <Text textAlign="center">
                Already have an account? <Link to="/login">Login</Link>
              </Text>
            </Stack>
          </Stack>
        </form>
      </Box>
    </Box>
  );
}

export default SignUp;
