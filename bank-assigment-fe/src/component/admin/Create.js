// src/components/Create.js
import React, { useState } from 'react';
import { Box, Button, FormControl, FormLabel, Input, VStack, Heading } from '@chakra-ui/react';
import axios from 'axios';

function Create() {
  const [formData, setFormData] = useState({ name: '', description: '' });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async () => {
    try {
      await axios.post('http://localhost:8080/api/v1/items', formData);
      alert('Item created successfully');
    } catch (error) {
      console.error('Error creating item:', error);
    }
  };

  return (
    <Box p={5}>
      <Heading mb={6}>Create New Item</Heading>
      <VStack spacing={4}>
        <FormControl id="name">
          <FormLabel>Name</FormLabel>
          <Input name="name" value={formData.name} onChange={handleChange} />
        </FormControl>
        <FormControl id="description">
          <FormLabel>Description</FormLabel>
          <Input name="description" value={formData.description} onChange={handleChange} />
        </FormControl>
        <Button colorScheme="teal" onClick={handleSubmit}>Submit</Button>
      </VStack>
    </Box>
  );
}

export default Create;
