import React, { useState, useEffect } from 'react';
import { Outlet } from 'react-router-dom';
import {
  Box,
  Button,
  Heading,
  Text,
  Container,
  Flex,
  Spinner,
  Modal,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  ModalBody,
  ModalCloseButton,
  useDisclosure,
  VStack,
  Divider,
  FormControl,
  FormLabel,
  Input,
  Stack,
} from '@chakra-ui/react';
import Swal from 'sweetalert2';
import axios from 'axios';
import ViewTransactions from './viewTransactions/ViewTransactions';
import ViewAccounts from './viewAccounts/ViewAccounts';
import CreateAccounts from './accounts/CreateAccounts';
import InitialDeposit from './deposit/InitialDeposit';

function Dashboard() {
  const [totalAccounts, setTotalAccounts] = useState(0);
  const [totalTransactions, setTotalTransactions] = useState(0);
  const [totalCredit, setTotalCredit] = useState(0);
  const [totalDebit, setTotalDebit] = useState(0);
  const [isLoading, setIsLoading] = useState(true);
  const [transactionData, setTransactionData] = useState({ toAccountNumber: '', amount: '' });
  const [balance, setBalance] = useState('N/A'); // State for balance

  const { isOpen: isTransactionsOpen, onOpen: onOpenTransactions, onClose: onCloseTransactions } = useDisclosure();
  const { isOpen: isAccountsOpen, onOpen: onOpenAccounts, onClose: onCloseAccounts } = useDisclosure();
  const { isOpen: isCreateAccountOpen, onOpen: onOpenCreateAccount, onClose: onCloseCreateAccount } = useDisclosure();
  const { isOpen: isDepositOpen, onOpen: onOpenDeposit, onClose: onCloseDeposit } = useDisclosure();
  const { isOpen: isTransactionOpen, onOpen: onOpenTransaction, onClose: onCloseTransaction } = useDisclosure(); // New transaction modal

  const fetchData = async () => {
    await new Promise(resolve => setTimeout(resolve, 2000));
    const mockAccounts = [
      { id: 1, createdBy: { uid: 'user1' } },
      { id: 2, createdBy: { uid: 'user1' } }
    ];

    const mockTransactions = [
      { id: 1, type: 'credit', amount: '100' },
      { id: 2, type: 'debit', amount: '50' },
      { id: 3, type: 'credit', amount: '200' }
    ];

    let credit = 0;
    let debit = 0;
    mockTransactions.forEach(transaction => {
      if (transaction.type === 'credit') {
        credit += parseInt(transaction.amount);
      } else {
        debit += parseInt(transaction.amount);
      }
    });

    setTotalAccounts(mockAccounts.length);
    setTotalTransactions(mockTransactions.length);
    setTotalCredit(credit);
    setTotalDebit(debit);
    setIsLoading(false);
  };

  useEffect(() => {
    fetchData();
    fetchBalance(); // Fetch balance on component mount
  }, []);

  const fetchBalance = async () => {
    const accountId = localStorage.getItem('accountId'); // Get accountId from localStorage
    const token = localStorage.getItem('token'); // Get token from localStorage

    console.log('Fetched accountId:', accountId); // Debugging line

    if (!accountId) {
      setBalance('N/A'); // No accountId, cannot fetch balance
      return;
    }

    try {
      const response = await axios.get(`http://localhost:8080/api/balance/${accountId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      // Assuming response.data is the object as shown in the API response
      const balanceData = response.data;
      const formattedBalance = balanceData ? `$${parseFloat(balanceData.amount).toFixed(2)}` : 'N/A';
      setBalance(formattedBalance); // Extract and format the balance amount
    } catch (error) {
      console.error('Error fetching balance:', error);
      setBalance('N/A'); // Default to 'N/A' in case of error
    }
  };

  const handleTransactionChange = (e) => {
    const { name, value } = e.target;
    setTransactionData({ ...transactionData, [name]: value });
  };

  const handleTransactionSubmit = async (e) => {
    e.preventDefault();
    const fromAccountID = localStorage.getItem('accountId');

    if (!transactionData.toAccountNumber || !transactionData.amount) {
      Swal.fire({
        title: 'Error',
        text: 'Please fill in all fields.',
        icon: 'error',
        confirmButtonText: 'OK'
      });
      return;
    }

    try {
      const token = localStorage.getItem('token');
      await axios.post('http://localhost:8080/api/transaction', {
        fromAccountID,
        toAccountNumber: transactionData.toAccountNumber,
        amount: transactionData.amount,
      }, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      Swal.fire({
        title: 'Transaction successful',
        icon: 'success',
        confirmButtonText: 'OK'
      });

      // Refresh balance after successful transaction
      fetchBalance();
      onCloseTransaction();
    } catch (error) {
      Swal.fire({
        title: 'Transaction failed',
        text: 'There was an issue processing the transaction.',
        icon: 'error',
        confirmButtonText: 'OK'
      });
    }
  };

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
        <Heading size="lg" mb={8} textAlign="center">Dashboard</Heading>
        <VStack spacing={4} align="start">
          <Button w="full" onClick={onOpenTransactions}>View Transactions</Button>
          <Button w="full" onClick={onOpenCreateAccount}>Create Account</Button>
          <Button w="full" colorScheme="teal" onClick={onOpenDeposit}>Initial Deposit</Button>
          <Button w="full" colorScheme="blue" onClick={onOpenTransaction}>Make Transaction</Button> {/* New button */}
        </VStack>
      </Box>

      {/* Main content */}
      <Box
        ml={{ base: '0', md: '20%' }}
        p={4}
        w={{ base: 'full', md: '80%' }}
        bg="white"
      >
        <Container maxW="container.xl" py={5}>
          <Flex wrap="wrap" gap={4}>
            <Box w="full" lg="48%" p={4} borderWidth={1} borderRadius="md" boxShadow="md">
              <Heading size="md" mb={4} textAlign="center">
                <i className="fa-solid fa-money-bill-1"></i> Transactions
              </Heading>
              <Button colorScheme="blue" mb={4} leftIcon={<i className="fa-solid fa-eye"></i>} onClick={onOpenTransactions}>
                View All Transactions
              </Button>
              <Divider />
              {isLoading ? (
                <Flex justify="center" align="center" h="full" p={4}>
                  <Spinner size="xl" />
                </Flex>
              ) : (
                <Box textAlign="center" mt={4}>
                  <Text fontSize="xl">Available Balance:</Text>
                  <Text fontSize="2xl" fontWeight="bold">{balance}</Text>
                </Box>
              )}
            </Box>
            <Box w="full" lg="48%" p={4} borderWidth={1} borderRadius="md" boxShadow="md">
              <Heading size="md" mb={4} textAlign="center">
                <i className="fa-solid fa-user"></i> Accounts
              </Heading>
              <Flex direction="column" align="center" mb={4}>
                <Button colorScheme="green" mb={2} leftIcon={<i className="fa-solid fa-plus"></i>} onClick={onOpenCreateAccount}>
                  Create Account
                </Button>
              </Flex>
              <Divider />
              {isLoading ? (
                <Flex justify="center" align="center" h="full" p={4}>
                  <Spinner size="xl" />
                </Flex>
              ) : (
                <Text fontSize="2xl" textAlign="center" mt={4}>
                  {/* Additional content if needed */}
                </Text>
              )}
            </Box>
          </Flex>
        </Container>

        <Outlet />
      </Box>

      {/* Modals */}
      <Modal isOpen={isTransactionsOpen} onClose={onCloseTransactions} size="xl">
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>All Transactions</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <ViewTransactions />
          </ModalBody>
        </ModalContent>
      </Modal>

      <Modal isOpen={isAccountsOpen} onClose={onCloseAccounts} size="xl">
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>All Accounts</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <ViewAccounts />
          </ModalBody>
        </ModalContent>
      </Modal>

      <Modal isOpen={isCreateAccountOpen} onClose={onCloseCreateAccount} size="xl">
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>Create Account</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <CreateAccounts />
          </ModalBody>
        </ModalContent>
      </Modal>

      <Modal isOpen={isDepositOpen} onClose={onCloseDeposit} size="xl">
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>Initial Deposit</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <InitialDeposit />
          </ModalBody>
        </ModalContent>
      </Modal>

      <Modal isOpen={isTransactionOpen} onClose={onCloseTransaction} size="xl">
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>Make Transaction</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <Box as="form" onSubmit={handleTransactionSubmit}>
              <Stack spacing={4}>
                <FormControl id="toAccountNumber" isRequired>
                  <FormLabel>Receiver Account Number</FormLabel>
                  <Input
                    type="text"
                    name="toAccountNumber"
                    value={transactionData.toAccountNumber}
                    onChange={handleTransactionChange}
                  />
                </FormControl>
                <FormControl id="amount" isRequired>
                  <FormLabel>Amount</FormLabel>
                  <Input
                    type="number"
                    name="amount"
                    value={transactionData.amount}
                    onChange={handleTransactionChange}
                  />
                </FormControl>
                <Button type="submit" colorScheme="blue">Submit</Button>
              </Stack>
            </Box>
          </ModalBody>
        </ModalContent>
      </Modal>
    </Flex>
  );
}

export default Dashboard;
