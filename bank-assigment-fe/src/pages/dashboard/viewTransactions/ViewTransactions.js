import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Table, Thead, Tbody, Tr, Th, Td } from 'react-super-responsive-table';
import 'react-super-responsive-table/dist/SuperResponsiveTableStyle.css';
import { Rings } from 'react-loader-spinner';
import { Button, Modal, ModalOverlay, ModalContent, ModalHeader, ModalBody, ModalFooter, useDisclosure } from '@chakra-ui/react';
import Swal from 'sweetalert2';
import './customTableStyles.css'; 

function ViewTransactions() {
  const [documents, setDocuments] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [docId, setDocId] = useState('');
  const [transactionDetail, setTransactionDetail] = useState({});
  const { isOpen, onOpen, onClose } = useDisclosure();

  const readDocs = () => {
    setIsLoading(true);
    // Mock data
    const mockData = [
      {
        id: '1',
        dateCreated: new Date(),
        accountId: '12345',
        type: 'Deposit',
        amount: '1000',
        description: 'Deposit to savings'
      },
      // Add more mock data as needed
    ];
    setDocuments(mockData);
    setIsLoading(false);
  };

  useEffect(() => {
    readDocs();
  }, []);

  useEffect(() => {
    const selectedDoc = documents.find((doc) => doc.id === docId);
    if (selectedDoc) {
      setTransactionDetail(selectedDoc);
    }
  }, [docId, documents]);

  const showAlert = (title, text) => {
    Swal.fire({
      title,
      text,
      icon: 'info',
      confirmButtonText: 'Close'
    });
  };

  return (
    <div className='p-4'>
      <div className="container mx-auto">
        <div className="bg-white shadow-md rounded-lg p-4">
          <div className="mb-4">
            <Link to="/dashboard">
              <Button colorScheme='red' leftIcon={<i className="fa-solid fa-arrow-left"></i>}>
                Dashboard
              </Button>
            </Link>
          </div>
          <div className="mb-4 text-center">
            <h5 className="text-xl font-semibold"><i className="fa-solid fa-money-bill-1"></i> Transactions</h5>
          </div>
          {isLoading
            ? <div className="flex justify-center items-center h-64">
              <Rings />
            </div>
            : <>
              {documents.length < 1
                ? <div className='text-center my-5'>
                  <p>We don't have any Transaction yet!</p>
                  <Link to="/dashboard/createAccounts">
                    <Button colorScheme='green'>
                      Create Account
                    </Button>
                  </Link>
                </div>
                : <Table className="customTable">
                  <Thead>
                    <Tr>
                      <Th>Transaction ID</Th>
                      <Th>Time</Th>
                      <Th>Date</Th>
                      <Th>Account ID</Th>
                      <Th>Type</Th>
                      <Th>Amount</Th>
                    </Tr>
                  </Thead>
                  <Tbody>
                    {
                      documents.map((doc, index) => (
                        <Tr key={index}>
                          <Td>
                            <Button variant='link' onClick={() => { setDocId(doc.id); onOpen(); }}>
                              {doc.id}
                            </Button>
                          </Td>
                          <Td>{doc.dateCreated.toLocaleTimeString('en-US')}</Td>
                          <Td>{doc.dateCreated.toDateString()}</Td>
                          <Td>{doc.accountId}</Td>
                          <Td>{doc.type}</Td>
                          <Td>{doc.amount}</Td>
                        </Tr>
                      ))
                    }
                  </Tbody>
                </Table>
              }
              <Modal isOpen={isOpen} onClose={onClose}>
                <ModalOverlay />
                <ModalContent>
                  <ModalHeader>Transaction Details</ModalHeader>
                  <ModalBody>
                    <div className='space-y-4'>
                      <div><strong>Account ID#:</strong> {transactionDetail.accountId}</div>
                      <div><strong>Account Holder Name#:</strong> {transactionDetail.fullName || 'N/A'}</div>
                      <div><strong>Transaction Date:</strong> {transactionDetail?.dateCreated?.toDateString()}</div>
                      <div><strong>Transaction Time:</strong> {transactionDetail?.dateCreated?.toLocaleTimeString('en-US')}</div>
                      <div><strong>Transaction Type:</strong> {transactionDetail.type}</div>
                      <div><strong>Amount:</strong> {transactionDetail.amount}</div>
                      <div><strong>Description:</strong> {transactionDetail.description}</div>
                    </div>
                  </ModalBody>
                  <ModalFooter>
                    <Button colorScheme='red' mr={3} onClick={onClose}>
                      Close
                    </Button>
                  </ModalFooter>
                </ModalContent>
              </Modal>
            </>
          }
        </div>
      </div>
    </div>
  );
}

export default ViewTransactions;
