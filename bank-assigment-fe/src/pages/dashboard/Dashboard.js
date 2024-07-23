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
} from '@chakra-ui/react';
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

  const { isOpen: isTransactionsOpen, onOpen: onOpenTransactions, onClose: onCloseTransactions } = useDisclosure();
  const { isOpen: isAccountsOpen, onOpen: onOpenAccounts, onClose: onCloseAccounts } = useDisclosure();
  const { isOpen: isCreateAccountOpen, onOpen: onOpenCreateAccount, onClose: onCloseCreateAccount } = useDisclosure();
  const { isOpen: isDepositOpen, onOpen: onOpenDeposit, onClose: onCloseDeposit } = useDisclosure(); // New deposit modal

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
          <Button w="full" onClick={onOpenAccounts}>View Accounts</Button>
          <Button w="full" onClick={onOpenCreateAccount}>Create Account</Button>
          <Button w="full" colorScheme="teal" onClick={onOpenDeposit}>Initial Deposit</Button> {/* New button */}
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
                  <Text fontSize="2xl" mb={2}>
                    {totalTransactions}
                  </Text>
                  <Flex justify="space-between" px={4}>
                    <Text fontSize="lg" color="green.500">
                      Total Credits Rs: {totalCredit}
                    </Text>
                    <Text fontSize="lg" color="red.500">
                      Total Debits Rs: {totalDebit}
                    </Text>
                  </Flex>
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
                <Button colorScheme="blue" onClick={onOpenAccounts} leftIcon={<i className="fa-solid fa-eye"></i>}>
                  View All Accounts
                </Button>
              </Flex>
              <Divider />
              {isLoading ? (
                <Flex justify="center" align="center" h="full" p={4}>
                  <Spinner size="xl" />
                </Flex>
              ) : (
                <Text fontSize="2xl" textAlign="center" mt={4}>
                  {totalAccounts}
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
            <ViewTransactions showModal={false} />
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
          <ModalHeader>Create New Account</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <CreateAccounts />
          </ModalBody>
        </ModalContent>
      </Modal>

      <Modal isOpen={isDepositOpen} onClose={onCloseDeposit} size="xl"> {/* New deposit modal */}
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>Initial Deposit</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <InitialDeposit />
          </ModalBody>
        </ModalContent>
      </Modal>
    </Flex>
  );
}

export default Dashboard;
