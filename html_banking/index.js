
        let maxId = 1;
        let customerId = 0;

        // const customers = [
        //     {
        //         id: maxId++,
        //         fullName: 'Đạt',
        //         email: 'dat@gmai.com',
        //         phone: '2345',
        //         address: 'PBC',
        //         balance: 500,
        //         deleted: 0
        //     },
        //     {
        //         id: maxId++,
        //         fullName: 'Quỳnh Anh',
        //         email: 'quynhanh@gmail.com',
        //         phone: '0773547101',
        //         address: 'ĐBP',
        //         balance: 100,
        //         deleted: 0
        //     },
        //     {
        //         id: maxId++,
        //         fullName: 'Trương Long',
        //         email: 'long@gmail.com',
        //         phone: '076516975',
        //         address: 'Dương Văn An',
        //         balance: 9999,
        //         deleted: 0
        //     },
        //     {
        //         id: maxId++,
        //         fullName: 'Phan Dũng',
        //         email: 'dung@gmail.com',
        //         phone: '0935156157',
        //         address: 'Bầu Vá',
        //         balance: 5000,
        //         deleted: 0
        //     },
        //     {
        //         id: maxId++,
        //         fullName: 'Minh Nguyệt',
        //         email: 'minhnguyet@gmail.com',
        //         phone: '0112233445',
        //         address: 'Tuần',
        //         balance: 100000,
        //         deleted: 0
        //     }
        // ]


            function renderCustomer(obj) {
                return `
                    <tr>
                        <th>${obj.id}</th>
                        <td>${obj.fullName}</td>
                        <td>${obj.email}</td>
                        <td>${obj.phone}</td>
                        <td>${obj.address}</td>
                        <td>${obj.balance}</td>
                        <td class="text-center">
                            <td>
                                 <button class="btn btn-outline-secondary edit" data-id="${obj.id}">
                                     <i class="fas fa-pencil-alt"></i>
                                </button>
                            </td>
                            <td>
                                 <button class="btn btn-outline-success deposit" data-id="${obj.id}">
                                    <i class="fas fa-plus"></i>
                                </button>
                            </td>
                            <td>
                                 <button class="btn btn-outline-warning withdraw" data-id="${obj.id}">
                                    <i class="fas fa-minus"></i>
                                </button>
                            </td>
                            <td>
                                 <button class="btn btn-outline-primary transfer" data-id="${obj.id}">
                                    <i class="fas fa-exchange-alt"></i>
                                </button>
                            </td>
                            <td>
                                 <button class="btn btn-outline-primary delete" data-id="${obj.id}">
                                    <i class="fas fa-ban"></i>
                                </button>
                            </td>
                        </td>
                    </tr>
                    `;
            }



        function getAllCustomers() {
            const tbCustomerBody = $('#tbCustomer tbody')

            tbCustomerBody.empty();

            $.ajax({
                type: 'GET',
                url: 'http://localhost:3300/customers?deleted=0'
            })
                .done((data) => {
                    data.forEach(item => {
                        const str = renderCustomer(item);
                        tbCustomerBody.prepend(str);

                        handleAddEventShowModalUpdate();
                        handleAddEventShowModalDeposit();
                    });
                })
                .fail((error) => {
                    console.log(error);
                })
        }

        getAllCustomers();


        const btnCreate = $('#btnCreate');
        btnCreate.on('click', function () {
            const fullName = $('#fullNameCre').val();
            const email = $('emailCre').val();
            const phone = $('phoneCre').val();
            const address = $('addressCre').val();
            const balance = 0;
            const deleted = 0;

            const obj = {
                fullName,
                email,
                phone,
                address,
                balance,
                deleted
            }

            $.ajax(
                {
                    Headers: {
                        'accept': 'aplication/json',
                        'content-type': 'aplication/json'
                    },
                    type: 'POST',
                    url: 'http://localhost:3300/customers',
                    data: JSON.stringify(obj)
                }
            )
            .done((data) => {
                const str = renderCustomer(data);
                const tbCustomerBody = $('tbCustomer tbody');
                tbCustomerBody.prepend(str);

                $('#modalCreate').modal('hide');
            })
            .fail((error) => {
                console.log(error);
            })
        })


        function getCustomerById(id) {
            return $.ajax({
                type: 'get',
                url: 'http://localhost:3300/customers/' + id,
            });
        }


        function findCustomerIndexById(id) {
            let index = -1;

            for (let i = 0; i < customers.length; i++) {
                if (customers[i].id === id) {
                    index = i;
                }
            }

            return index;
        }



        function handleAddEventShowModalUpdate() {
            let btnEdit = $('.edit');
            btnEdit.off('click');
            btnEdit.on('click', function () {
                customerId = $(this).data('id');

                const modalUpdate = $('#modalUpdate');

                getCustomerById(customerId).then((data) => {
                    $('#fullNameUp').val(data.fullName);
                    $('#emailUp').val(data.email);
                    $('#phoneUp').val(data.phone);
                    $('#addressUp').val(data.address);

                    modalUpdate.modal('show');
                })
                    .catch((error) => {
                        console.log(error);
                    });
            })
        }

        handleAddEventShowModalUpdate();

        function handleAddEventShowModalUpdate() {
            let btnEdit = $('.edit');
            btnEdit.off('click');
            btnEdit.on('click', function () {
                customerId = $(this).data('id');

                const modalUpdate = $('#modalUpdate');

                getCustomerById(customerId).then((data) => {
                    $('#fullNameUp').val(data.fullName);
                    $('#emailUp').val(data.email);
                    $('#phoneUp').val(data.phone);
                    $('#addressUp').val(data.address);

                    modalUpdate.modal('show');
                })
                    .catch((error) => {
                        console.log(error);
                    });
            })
        }


        function update(obj) {
            $.ajax({
                headers: {
                    'accept': 'application/json',
                    'content-type': 'application/json'
                },
                type: 'PATCH',
                url: 'http://localhost:3300/customers/' + customerId,
                data: JSON.stringify(obj)
            })
                .done((data) => {
                    const str = renderCustomer(data);

                    const currentRow = $('#tr_' + customerId);
                    currentRow.replaceWith(str);

                    $('#modalUpdate').modal('hide');
                })
                .fail((error) => {
                    console.log(error);
                })
        }



        function handleAddEventShowModalDeposit() {
            let btnDeposit = $('.deposit');
            btnDeposit.on('click', function () {
                customerId = $(this).data('id');

                const modalDeposit = $('#modalDeposit');

                getCustomerById(customerId).then((data) => {
                    customer = data;
                    $('#fullNameDep').val(customer.fullName);
                    $('#emailDep').val(customer.email);
                    $('#balanceDep').val(customer.balance);

                    modalDeposit.modal('show');
                })
                    .catch((error) => {
                        console.log(error);
                    });
            })
        }

            handleAddEventShowModalDeposit();

            const btnDeposit = $('#btnDeposit');
            btnDeposit.on('click', () => {
    
                const currentBalance = customer.balance;
                const transactionAmount = +$('#transactionAmountDep').val();
                const newBalance = currentBalance + transactionAmount;
                customer.balance = newBalance;
    
                $.ajax({
                    headers: {
                        'accept': 'application/json',
                        'content-type': 'application/json'
                    },
                    type: 'PATCH',
                    url: 'http://localhost:3300/customers/' + customerId,
                    data: JSON.stringify(customer)
                })
                    .done((data) => {
                        const str = renderCustomer(data);
    
                        const currentRow = $('#tr_' + customerId);
                        currentRow.replaceWith(str);
    
                        handleAddEventShowModalUpdate();
                        handleAddEventShowModalDeposit();
    
                        $('#modalDeposit').modal('hide');
    
                        Swal.fire({
                            position: 'top-end',
                            icon: 'success',
                            title: 'Nạp tiền thành công',
                            showConfirmButton: false,
                            timer: 1500
                        })
                    })
                    .fail((error) => {
                        console.log(error);
                    })
    
    
                const obj = {
                    customerId,
                    transactionAmount
                }
    
                $.ajax({
                    headers: {
                        'accept': 'application/json',
                        'content-type': 'application/json'
                    },
                    type: 'POST',
                    url: 'http://localhost:3300/deposits',
                    data: JSON.stringify(obj)
                })
                    .done((data) => {
    
                    })
                    .fail((error) => {
                        console.log(error);
                    })
            })


            function handleAddEventShowModalWithdraw() {
                let btnWithdraw = document.querySelectorAll('.withdraw');

                btnWithdraw.forEach(item => {
                    item.addEventListener('click', function () {
                        customerId = +item.getAttribute('data-id');
                        const obj = getCustomerById(customerId);

                        document.getElementById('idWithdraw').value = obj.id;
                        document.getElementById('fullNameWithdraw').value = obj.fullName;
                        document.getElementById('emailWithdraw').value = obj.email;
                        document.getElementById('phoneWithdraw').value = obj.phone;
                        document.getElementById('addressWithdraw').value = obj.address;
                        document.getElementById('balanceWithdraw').value = obj.balance;

                        const modalWithdraw = new bootstrap.Modal(document.getElementById('modalWithdraw'), {
                            keyboard: false
                        });

                        modalWithdraw.show();
                    });
                });
            }

            handleAddEventShowModalWithdraw();

            const btnWithdraw = document.getElementById('btnWithdraw');
            btnWithdraw.addEventListener('click', function () {
                const fullName = document.getElementById('fullNameWithdraw').value;
                const email = document.getElementById('emailWithdraw').value;
                const phone = document.getElementById('phoneWithdraw').value;
                const address = document.getElementById('addressWithdraw').value;
                const balance = document.getElementById('balanceWithdraw').value;
                const transactionAmount = document.getElementById('transactionAmountWithdraw').value;

                // Nạp tiền vào tài khoản       
                const updatedBalance = parseFloat(balance) - Math.abs(parseFloat(transactionAmount));
                const obj = {
                    id: customerId,
                    fullName,
                    email,
                    phone,
                    address,
                    balance: updatedBalance
                };

                document.getElementById('balanceWithdraw').value = updatedBalance;

                const index = findCustomerIndexById(customerId);

                customers[index] = obj;

                // Cập nhật danh sách khách hàng
                getAllCustomers();

                handleAddEventShowModalWithdraw();
                handleAddEventShowModalDeposit();
                handleAddEventShowModalUpdate();
                handleAddEventModalDelete();
            });













            function handleAddEventShowModalTransfer() {
                const btnTransfer = document.querySelectorAll('.transfer');
                const recipientSelect = document.getElementById('recipientName');

                btnTransfer.forEach(item => {
                    item.addEventListener('click', function () {
                    const senderId = +item.getAttribute('data-id');
                    const sender = getCustomerById(senderId);

                    document.getElementById('senderID').value = sender.id;
                    document.getElementById('senderName').value = sender.fullName;
                    document.getElementById('senderEmail').value = sender.email;
                    document.getElementById('senderPhone').value = sender.phone;
                    document.getElementById('senderAddress').value = sender.address;
                    document.getElementById('senderBalance').value = sender.balance;

                    
                    recipientSelect.innerHTML = '';

                    
                    for (let i = 0; i < customers.length; i++) {
                        const customer = customers[i];
                       
                        if (customer.id !== senderId) {
                        const option = document.createElement('option');
                        option.value = customer.fullName;
                        option.text = customer.fullName;
                        recipientSelect.appendChild(option);
                        }
                    }

                    const modalTransfer = new bootstrap.Modal(document.getElementById('modalTransfer'), {
                        keyboard: false
                    });

                    modalTransfer.show();
                    });
                });
                }

                handleAddEventShowModalTransfer();

                    const btnTransfer = document.getElementById('btnTransfer');
                    btnTransfer.addEventListener('click', function () {
                    const senderId = document.getElementById('senderId').value;
                    const recipientId = document.getElementById('recipientName').value;
                    const transferAmount = parseFloat(document.getElementById('transferAmount').value);

                    const senderName = document.getElementById('senderName').value;
                    const recipient = document.getElementById('recipientName').value;
                    const recipientBalance = document.getElementById('recipientBalance').value;

                    if (sender && recipient) {
                        if (sender.balance >= transferAmount) {
                        sender.balance -= transferAmount;
                        recipientBalance += transferAmount;

                        updateCustomer(sender);
                        updateCustomer(recipient);
                        } else {
                        alert('Số dư không đủ để thực hiện giao dịch!');
                        }
                    }

                    const sender = {
                        id: customerId,
                        fullName,
                        email,
                        phone,
                        address,
                        balance: updatedBalance
                    };

                    document.getElementById('balanceWithdraw').value = updatedBalance;

                    const index = findCustomerIndexById(customerId);

                    customers[index] = obj;

                    getAllCustomers();

                    handleAddEventShowModalWithdraw();
                    handleAddEventShowModalDeposit();
                    handleAddEventShowModalUpdate();
                    handleAddEventModalDelete();

                    });









            // function handleAddEventShowModalTransfer() {
            //     let btnTransfer = document.querySelectorAll('.transfer');

            //     btnTransfer.forEach(item => {
            //         item.addEventListener('click', function () {
            //             customerId = +item.getAttribute('data-id');
            //             const obj = getCustomerById(customerId);

            //             document.getElementById('senderId').value = obj.id;
            //             document.getElementById('senderName').value = obj.fullName;
            //             document.getElementById('senderEmail').value = obj.email;
            //             document.getElementById('senderBalance').value = obj.balance;

            //             const modalTransfer = new bootstrap.Modal(document.getElementById('modalTransfer'), {
            //                 keyboard: false
            //             });

            //             modalTransfer.show();
            //         });
            //     });
            // }


//                 handleAddEventShowModalTransfer();

//                 const btnTransfer = document.getElementById('btnTransfer');


//                 btnTransfer.addEventListener('click', function () {
//                 const senderId = document.getElementById('senderId').value;
//                 const senderName = document.getElementById('senderName').value;
//                 const senderEmail = document.getElementById('senderEmail').value;
//                 const senderBalance = parseFloat(document.getElementById('senderBalance').value);
//                 const recipientName = document.getElementById('recipientName').value;
//                 const transferAmount = parseFloat(document.getElementById('transferAmount').value);
//                 const feePercentage = 10;
//                 const feeAmount = (transferAmount * feePercentage) / 100;
//                 const totalAmount = transferAmount + feeAmount;

//                 // Kiểm tra xem người chuyển có đủ số dư để thực hiện giao dịch không
//                 if (senderBalance >= totalAmount) {
//                     // Trừ số dư của người chuyển
//                     const updatedSenderBalance = senderBalance - totalAmount;

//                     const sender = {
//                     id: senderId,
//                     fullName: senderName,
//                     email: senderEmail,
//                     balance: updatedSenderBalance
//                     };

//                     // Cập nhật số dư của người chuyển
//                     document.getElementById('senderBalance').value = updatedSenderBalance;

//                     // Tìm thông tin người nhận theo tên
//                     const recipient = findCustomerByName(recipientName);

//                     if (recipient) {
//                     // Cộng số dư cho người nhận
//                     const updatedRecipientBalance = recipient.balance + transferAmount;

//                     const updatedRecipient = {
//                         id: recipient.id,
//                         fullName: recipient.fullName,
//                         email: recipient.email,
//                         balance: updatedRecipientBalance
//                     };

//                     // Cập nhật số dư của người nhận
//                     updateCustomer(updatedRecipient);

//                     // Hiển thị thông báo thành công
//                     alert('Chuyển tiền thành công!');
//                     } else {
//                     // Hiển thị thông báo người nhận không tồn tại
//                     alert('Người nhận không tồn tại!');
//                     }

//                     // Cập nhật thông tin người chuyển
//                     updateCustomer(sender);
//                 } else {
//                     // Hiển thị thông báo không đủ số dư
//                     alert('Số dư không đủ để thực hiện giao dịch!');
//                 }

//                 // Cập nhật danh sách khách hàng
//                 getAllCustomers();

//                 handleAddEventShowModalWithdraw();
//                 handleAddEventShowModalDeposit();
//                 handleAddEventShowModalUpdate();
//                 handleAddEventModalDelete();
// });












            function handleAddEventModalDelete() {
                $('.delete').on('click', function () {
                    id = $(this).data('id');

                    Swal.fire({
                        title: 'Are you sure?',
                        text: "You won't be able to revert this!",
                        icon: 'warning',
                        showCancelButton: true,
                        confirmButtonColor: '#3085d6',
                        cancelButtonColor: '#d33',
                        confirmButtonText: 'Yes, delete it!'
                    }).then((result) => {
                        if (result.isConfirmed) {
                            customers = customers.filter(item => item.id != id);

                            // Cập nhật danh sách khách hàng
                            $('#tr_' + id).remove();

                            Swal.fire({
                                position: 'top-end',
                                icon: 'success',
                                title: 'Your work has been saved',
                                showConfirmButton: false,
                                timer: 1500
                            });
                        }
                    });
                });
            }

            handleAddEventModalDelete();





  















           
    //         let id = 1;
    //         let customers = [
    //             new Customer(id++, 'Phước Đạt', 'dat@gmail.com', '0856419555', 'Hue', 0, false),
    //             new Customer(id++, 'Quỳnh Anh', 'quynhanh@gmail.com', '0773547101', 'BĐP', 0, false)
    //         ];
    
    //         let tbody = $('#tbCustomer tbody');
    
    //         function renderCustomer(obj) {
    //             return `
    //                 <tr id='tr_${obj.id}'>
    //                     <td>${obj.id}</td>
    //                     <td>${obj.fullName}</td>
    //                     <td>${obj.email}</td>
    //                     <td>${obj.phone}</td>
    //                     <td>${obj.address}</td>
    //                     <td>${obj.balance}</td>
    //                     <td>
    //                         <button class='btn btn-outline-secondary edit' data-id='${obj.id}'>
    //                              <i class="fas fa-pencil-alt"></i>
    //                          </button>
    //                     </td>
    //                     <td>
    //                          <button class="btn btn-outline-success deposit" data-id='${obj.id}' >
    //                              <i class="fas fa-plus"></i>
    //                         </button>
    //                     </td>
    //                     <td>
    //                         <button class="btn btn-outline-primary withdraw" data-id='${obj.id}' >
    //                              <i class="fas fa-minus"></i>
    //                       </button>
    //                     </td>
    //                     <td>
    //                         <button class="btn btn-outline-warning transfer" data-id='${obj.id}' >
    //                             <i class="fas fa-exchange-alt"></i>
    //                          </button>
    //                     </td>
    //                     <td>
    //                           <button class='btn btn-outline-danger delete' data-id='${obj.id}'>
    //                             <i class="fas fa-ban"></i>
    //                       </button>
    //                     </td>
    //                 </tr>
    //             `;
    //         }
    
    
    //         function getAllCustomers() {
    //             customers.forEach((customer) => {
    //                 let str = renderCustomer(customer);
    //                 tbody.prepend(str);
    //             });
    
    //             addEventEdit();
    //             addEventDeposit();
    //             addEventDelete();
    //         }
    
    //         getAllCustomers();
    
    //         function getCustomerById(customerId) {
    //             return customers.find(item => item.id == customerId);
    //         }
    
    
    //         function addEventEdit() {
    //             $('.edit').on('click', function () {
    //                 customerId = $(this).data('id');
    //                 let customer = getCustomerById(customerId);
    //                 if (customer) {
    //                     $('#fullNameUp').val(customer.fullName);
    //                     $('#emailUp').val(customer.email);
    //                     $('#phoneUp').val(customer.phone);
    //                     $('#addressUp').val(customer.address);
    //                     $('#mdEdit').modal('show');
    //                 }
    //             });
    //         }
    
    
    //         function addEventDeposit() {
    //   $('.deposit').on('click', function() {
    //     customerId = $(this).data('id');
    //     let customer = getCustomerById(customerId);
    //     if (customer) {
    //       $('#fullNameDeposit').text(customer.fullName);
    //       $('#customerIdDeposit').val(customerId);
    //       $('#depositAmount').val('');
    //       $('#mdDeposit').modal('show');
    //     }
    //   });
    
    //   $('#depositForm').on('submit', function(e) {
    //     e.preventDefault();
    //     let customerId = $('#customerIdDeposit').val();
    //     let depositAmount = parseFloat($('#depositAmount').val());
    //     let customer = getCustomerById(customerId);
    //     if (customer) {
    //       customer.balance += depositAmount;
    
    //       $('#balanceDisplay').text(customer.balance);
    
    //       $('#mdDeposit').modal('hide');
    //     }
    //   });
    // }
    
    
    
    //         function addEventDelete() {
    //             $('.delete').on('click', function () {
    
    //                 customerId = $(this).data('id');
    
    //                 Swal.fire({
    //                     title: 'Are you sure?',
    //                     text: "You won't be able to revert this!",
    //                     icon: 'warning',
    //                     showCancelButton: true,
    //                     confirmButtonColor: '#3085d6',
    //                     cancelButtonColor: '#d33',
    //                     confirmButtonText: 'Yes, delete it!'
    //                 }).then((result) => {
    //                     if (result.isConfirmed) {
    
    //                         customers = customers.filter(item => item.id != customerId);
    
    //                         $('#tr_' + customerId).remove();
    
    //                         Swal.fire({
    //                             position: 'top-end',
    //                             icon: 'success',
    //                             title: 'Your work has been saved',
    //                             showConfirmButton: false,
    //                             timer: 1500
    //                         })
    //                     }
    //                 })
    
    //             })
    //         }
    
    //         $('#btnCreate').on('click', () => {
    //             let fullName = $('#fullNameCre').val();
    //             let email = $('#emailCre').val();
    //             let phone = $('#phoneCre').val();
    //             let address = $('#addressCre').val();
    //             let balance = 0
    
    //             let customer = {
    //                 id,
    //                 fullName,
    //                 email,
    //                 phone,
    //                 address,
    //                 balance,
    //             }
    
    //             ++id;
    
    //             customers.push(customer);
    
    //             let str = renderCustomer(customer);
    //             $('#tbCustomer tbody').prepend(str);
    
    //             addEventEdit();
    //             addEventDeposit();
    //             addEventDelete();
    
    //         })
    
    
    //         $('#btnUpdate').on('click', () => {
    //             let fullName = $('#fullNameUp').val();
    //             let email = $('#emailUp').val();
    //             let phone = $('#phoneUp').val();
    //             let address = $('#addressUp').val();
    //             let balance = 0;
    //             let deleted = false;
    
    //             let customer = {
    //                 id: customerId,
    //                 fullName,
    //                 email,
    //                 phone,
    //                 address,
    //                 balance,
    //                 deleted
    //             }
    
    //             handleUpdateCustomer(customer).then((data) => {
    //                 let str = renderCustomer(data);
    //                 $('#tr_' + customerId).replaceWith(str);
    
    //                 addEventEdit();
    //                 addEventDelete();
    
    //                 $('#mdEdit').modal('hide');
    //             })
    //                 .catch((error) => {
    //                     console.log(error);
    //                 })
    //         })
    
    
    //         function handleUpdateCustomer(obj) {
    //             return new Promise((resolve, reject) => {
    //                 const index = customers.findIndex(item => item.id === obj.id);
    //                 if (index === -1) {
    //                     reject(new Error('Customer not found'));
    //                     return;
    //                 }
    //                 customers[index].fullName = obj.fullName;
    //                 customers[index].email = obj.email;
    //                 customers[index].phone = obj.phone;
    //                 customers[index].address = obj.address;
    
    //                 resolve(customers[index]);
    //             });
    //         }
    
    
    
       