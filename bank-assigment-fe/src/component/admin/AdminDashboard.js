import React, { useState } from 'react';
import { Outlet } from 'react-router-dom';
import {
  Box,
  Button,
  Heading,
  Flex,
  VStack,
} from '@chakra-ui/react';
import { Link } from 'react-router-dom';

function AdminDashboard() {
  return (
    <Flex minH="100vh">
      {/* Sidebar */}
      <Box
        w={{ base: 'full', md: '20%' }}
        p={4}
        bg="gray.100"
        borderRightWidth="1px"
        height="100vh"
        position={{ base: 'relative', md: 'fixed' }}
        top="0"
        left="0"
        zIndex="1"
      >
        <Heading size="lg" mb={8} textAlign="center">Admin Dashboard</Heading>
        <VStack spacing={4} align="start">
          <Link to="/admin/create">
            <Button w="full" colorScheme="teal">Create New Item</Button>
          </Link>
          <Link to="/admin/read">
            <Button w="full" colorScheme="blue">View Items</Button>
          </Link>
          <Link to="/admin/update">
            <Button w="full" colorScheme="yellow">Update Item</Button>
          </Link>
          <Link to="/admin/delete">
            <Button w="full" colorScheme="red">Delete Item</Button>
          </Link>
        </VStack>
      </Box>

      {/* Main content */}
      <Box
        ml={{ base: '0', md: '20%' }}
        p={4}
        w={{ base: 'full', md: '80%' }}
        bg="white"
      >
        <Outlet />
      </Box>
    </Flex>
  );
}

export default AdminDashboard;
