import React, { useState, useEffect } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';
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
  Image,
  Stack,
} from '@chakra-ui/react';
import Swal from 'sweetalert2';
import axios from 'axios';
import Cookies from 'js-cookie';
import ViewTransactions from './viewTransactions/ViewTransactions';
import CreateAccounts from './accounts/CreateAccounts';
import InitialDeposit from './deposit/InitialDeposit';

function Dashboard() {
  const [totalAccounts, setTotalAccounts] = useState(0);
  const [totalTransactions, setTotalTransactions] = useState(0);
  const [totalCredit, setTotalCredit] = useState(0);
  const [totalDebit, setTotalDebit] = useState(0);
  const [isLoading, setIsLoading] = useState(true);
  const [transactionData, setTransactionData] = useState({ toAccountNumber: '', amount: '' });
  const [balance, setBalance] = useState('N/A');
  const [isBalanceVisible, setIsBalanceVisible] = useState(false);

  const { isOpen: isTransactionsOpen, onOpen: onOpenTransactions, onClose: onCloseTransactions } = useDisclosure();
  const { isOpen: isCreateAccountOpen, onOpen: onOpenCreateAccount, onClose: onCloseCreateAccount } = useDisclosure();
  const { isOpen: isDepositOpen, onOpen: onOpenDeposit, onClose: onCloseDeposit } = useDisclosure();
  const { isOpen: isTransactionOpen, onOpen: onOpenTransaction, onClose: onCloseTransaction } = useDisclosure();

  const navigate = useNavigate();
  const accountId = Cookies.get('accountId');
  const token = Cookies.get('token');
  const username = Cookies.get('username');

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
  }, []);

  const fetchBalance = async () => {
    console.log("Account Id in fetch balance: ", accountId);

    if (!accountId) {
      setBalance('N/A');
      return;
    }

    try {
      const response = await axios.get(`http://localhost:8080/api/balance/${accountId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      const balanceData = response.data;
      const formattedBalance = balanceData ? `$${parseFloat(balanceData.amount).toFixed(2)}` : 'N/A';
      setBalance(formattedBalance);
    } catch (error) {
      console.error('Error fetching balance:', error);
      setBalance('N/A');
    }
  };

  const handleTransactionChange = (e) => {
    const { name, value } = e.target;
    setTransactionData({ ...transactionData, [name]: value });
  };

  const handleTransactionSubmit = async (e) => {
    e.preventDefault();
    const fromAccountID = Cookies.get('accountId');

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
      const token = Cookies.get('token');
      console.log("Sending request with:", {
        fromAccountID,
        toAccountNumber: transactionData.toAccountNumber,
        amount: transactionData.amount,
      });

      const response = await axios.post('http://localhost:8080/api/transaction', {
        fromAccountID,
        toAccountNumber: transactionData.toAccountNumber,
        amount: transactionData.amount,
      }, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      console.log("Response:", response);

      Swal.fire({
        title: 'Transaction successful',
        icon: 'success',
        confirmButtonText: 'OK'
      });

      fetchBalance();
      onCloseTransaction();
    } catch (error) {
      console.error("Transaction Error:", error);
      Swal.fire({
        title: 'Transaction failed',
        text: 'There was an issue processing the transaction.',
        icon: 'error',
        confirmButtonText: 'OK'
      });
    }
  };

  const handleShowBalance = () => {
    setIsBalanceVisible(true);
    fetchBalance();
  };

  const handleLogout = () => {
    Cookies.remove('token');
    Cookies.remove('userId');
    Cookies.remove('accountId');
    Cookies.remove('username');
    Cookies.remove('role');
    console.log("Logged out, cookies removed");
    navigate('/home'); 
  };

  return (
    <Flex minH="100vh">
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
          {/* <Button w="full" onClick={onOpenTransactions}>View Transactions</Button> */}
          <Button w="full" onClick={onOpenCreateAccount}>Create Account</Button>
          <Button w="full" colorScheme="teal" onClick={onOpenDeposit}>Initial Deposit</Button>
          <Button w="full" colorScheme="blue" onClick={onOpenTransaction}>Make Transaction</Button>
          <Button w="full" colorScheme="red" onClick={handleLogout}>Logout</Button>
        </VStack>
      </Box>

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
                <i className="fa-solid fa-money-bill-1"></i> Welcome to Bank App "{username}"
              </Heading>
              <Divider />
            </Box>
          </Flex>
          <Outlet />
        </Container>
        <Container maxW="container.xl" py={5}>
          <Flex wrap="wrap" gap={4}>
            <Box w="full" lg="48%" p={4} borderWidth={1} borderRadius="md" boxShadow="md">
              <Heading size="md" mb={4} textAlign="center">
                <i className="fa-solid fa-money-bill-1"></i> Transactions
              </Heading>
              <Button
  colorScheme="blue"
  mb={4}
  leftIcon={<i className="fa-solid fa-eye"></i>}
  display="block"
  mx="auto"
  onClick={onOpenTransactions}
>
  View All Transactions
</Button>

              <Divider />
              {isLoading ? (
                <Flex justify="center" align="center" h="full" p={4}>
                  <Spinner size="xl" />
                </Flex>
              ) : (
                <Box textAlign="center" mt={4}>
                  <Button colorScheme="teal" onClick={handleShowBalance}>
                    Show Balance
                  </Button>
                  {isBalanceVisible && (
                    <Box mt={4}>
                      <Text fontSize="xl">Available Balance:</Text>
                      <Text fontSize="2xl" fontWeight="bold">{balance}</Text>
                    </Box>
                  )}
                </Box>
              )}
            </Box>

          </Flex>

          <Outlet />
          <Image
  src="https://images.pexels.com/photos/164527/pexels-photo-164527.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
  alt="Bank Image"
  boxSize="100%"
  objectFit="cover"
  borderRadius="md"
  mt={4}
  filter="blur(5px)"  // 5px blur is approximately 50% blur effect
/>

        </Container>
        <Box
        as="footer"
        mt="auto"
        textAlign="center"
        // position="relative"
        bottom="0"
        w="full"
        bg="gray"
        color="white"
        py={4}
        px={8}
      >
        <Text>&copy; {new Date().getFullYear()} My Bank App. All rights reserved.</Text>
      </Box>
      </Box>



      {/* Modals */}
      <Modal isOpen={isTransactionsOpen} onClose={onCloseTransactions} size="lg">
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>View Transactions</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <ViewTransactions />
          </ModalBody>
        </ModalContent>
      </Modal>

      <Modal isOpen={isCreateAccountOpen} onClose={onCloseCreateAccount} size="lg">
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>Create Account</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <CreateAccounts />
          </ModalBody>
        </ModalContent>
      </Modal>

      <Modal isOpen={isDepositOpen} onClose={onCloseDeposit} size="lg">
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>Initial Deposit</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <InitialDeposit />
          </ModalBody>
        </ModalContent>
      </Modal>

      <Modal isOpen={isTransactionOpen} onClose={onCloseTransaction} size="lg">
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>Make Transaction</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <Box as="form" onSubmit={handleTransactionSubmit}>
              <FormControl id="toAccountNumber" mb={4}>
                <FormLabel>Receiver Account Number</FormLabel>
                <Input
                  type="text"
                  name="toAccountNumber"
                  value={transactionData.toAccountNumber}
                  onChange={handleTransactionChange}
                />
              </FormControl>
              <FormControl id="amount" mb={4}>
                <FormLabel>Amount</FormLabel>
                <Input
                  type="number"
                  name="amount"
                  value={transactionData.amount}
                  onChange={handleTransactionChange}
                />
              </FormControl>
              <Stack spacing={4} align="center">
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
