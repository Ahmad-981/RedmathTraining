import React, { useState } from 'react';
import axios from 'axios';
import { Box, Button, FormControl, FormLabel, Input, Select, VStack } from "@chakra-ui/react";
import Swal from 'sweetalert2';

const InitialDeposit = ({ onClose }) => {
  const [amount, setAmount] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();

    const parsedAmount = parseFloat(amount);
    if (isNaN(parsedAmount) || parsedAmount <= 0) {
      Swal.fire({
        title: 'Invalid Amount',
        text: 'Please enter a valid amount.',
        icon: 'error',
        confirmButtonText: 'OK',
        backdrop: true,
        allowOutsideClick: false
      });
      return;
    }

    if (parsedAmount > 100000) {
      Swal.fire({
        title: 'Amount Too High',
        text: 'The amount should not be more than 100,000.',
        icon: 'error',
        confirmButtonText: 'OK',
        backdrop: true,
        allowOutsideClick: false
      });
      return;
    }

    const token = localStorage.getItem('token');
    const accountID = localStorage.getItem('accountID');

    if (!token || !accountID) {
      Swal.fire({
        title: 'Authentication Error',
        text: 'User is not authenticated or accountID is missing.',
        icon: 'error',
        confirmButtonText: 'OK',
        backdrop: true,
        allowOutsideClick: false
      });
      return;
    }

    try {
      setIsLoading(true);
      await axios.post('http://localhost:8080/api/balance', {
        amount: parsedAmount,
        indicator: 'credit',
        accountID: accountID,
      }, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      Swal.fire({
        title: 'Deposit Successful',
        text: 'The initial deposit has been successfully made.',
        icon: 'success',
        timer: 3000, // Auto-dismiss after 3 seconds
        timerProgressBar: true,
        backdrop: true,
        allowOutsideClick: false
      }).then(() => {
        setAmount(''); // Reset the form fields
        if (onClose) onClose();
      });
    } catch (error) {
      Swal.fire({
        title: 'Error',
        text: 'An error occurred while making the deposit.',
        icon: 'error',
        confirmButtonText: 'OK',
        backdrop: true,
        allowOutsideClick: false
      });
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Box p={4}>
      <form onSubmit={handleSubmit}>
        <VStack spacing={4}>
          <FormControl isRequired>
            <FormLabel>Amount</FormLabel>
            <Input
              type="number"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              min="0"
              step="0.01"
            />
          </FormControl>
          <FormControl isRequired>
            <FormLabel>Indicator</FormLabel>
            <Select value="credit" isReadOnly>
              <option value="credit">Credit</option>
            </Select>
          </FormControl>
          <Button type="submit" colorScheme="blue" isLoading={isLoading}>
            Submit
          </Button>
        </VStack>
      </form>
    </Box>
  );
};

export default InitialDeposit;
