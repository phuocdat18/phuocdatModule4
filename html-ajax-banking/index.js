let maxId = 1;
let customerId = 0;


function renderCustomer(obj) {
    return `
            <tr id="tr_${obj.id}">
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
                         <button class="btn btn-outline-danger delete" data-id="${obj.id}">
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
            });

            handleAddEventShowModalUpdate();
            handleAddEventShowModalDeposit();
            handleAddEventShowModalWithdraw();
            handleAddEventModalDelete();
            handleAddEventShowModalTransfer();
        })
        .fail((error) => {
            console.log(error);
        })
}

getAllCustomers();


const btnCreate = $('#btnCreate');
btnCreate.on('click', function () {
    const fullName = $('#fullNameCre').val();
    const email = $('#emailCre').val(); 
    const phone = $('#phoneCre').val(); 
    const address = $('#addressCre').val(); 
    const balance = 0;
    const deleted = 0;

    const obj = {
        fullName,
        email,
        phone,
        address,
        balance,
        deleted
    };

    $.ajax({
        headers: {
            'Accept': 'application/json', 
            'Content-Type': 'application/json' 
        },
        type: 'POST',
        url: 'http://localhost:3300/customers',
        data: JSON.stringify(obj)
    })
    .done((data) => {
        const str = renderCustomer(data);
        const tbCustomerBody = $('tbCustomer tbody');
        tbCustomerBody.prepend(str);

        $('#modalCreate').modal('hide');
    })
    .fail((error) => {
        console.log(error);
    });
});



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


function handleAddEventShowModalDeposit() {
    let btnDeposit = $('.deposit');
    btnDeposit.on('click', function () {
        customerId = $(this).data('id');

        const modalDeposit = $('#modalDeposit');

        getCustomerById(customerId).then((data) => {
            customer = data;
            $('#idDeposit').val(customer.id);    
            $('#fullNameDeposit').val(customer.fullName);
            $('#emailDeposit').val(customer.email);
            $('#balanceDeposit').val(customer.balance);

            modalDeposit.modal('show');
        })
            .catch((error) => {
                console.log(error);
            });
    })
}


function handleAddEventShowModalWithdraw() {
    let btnWithdraw = $('.withdraw');
    btnWithdraw.on('click', function () {
        customerId = $(this).data('id');

        const modalWithdraw = $('#modalWithdraw');

        getCustomerById(customerId).then((data) => {
            customer = data;
            $('#idWithdraw').val(customer.id);    
            $('#fullNameWithdraw').val(customer.fullName);
            $('#emailWithdraw').val(customer.email);
            $('#balanceWithdraw').val(customer.balance);

            modalWithdraw.modal('show');
        })
            .catch((error) => {
                console.log(error);
            });
    })
}


const btnUpdate = $('#btnUpdate');
btnUpdate.on('click', () => {
    const fullName = $('#fullNameUp').val();
    const email = $('#emailUp').val();
    const phone = $('#phoneUp').val();
    const address = $('#addressUp').val();

    const obj = {
        fullName,
        email,
        phone,
        address,
        balance
    }

    update(obj);
})


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


const btnDeposit = $('#btnDeposit');
btnDeposit.on('click', () => {

    const currentBalance = customer.balance;
    const transactionAmount = +$('#transactionAmountDeposit').val();
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
            handleAddEventShowModalWithdraw();
            handleAddEventModalDelete();
            handleAddEventShowModalTransfer();

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



const btnWithdraw = $('#btnWithdraw');
btnWithdraw.on('click', () => {

    const currentBalance = customer.balance;
    const transactionAmount = +$('#transactionAmountWithdraw').val();
    const newBalance = currentBalance - transactionAmount;
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
            handleAddEventShowModalWithdraw();
            handleAddEventModalDelete();
            handleAddEventShowModalTransfer();

            $('#modalWithdraw').modal('hide');

            Swal.fire({
                position: 'top-end',
                icon: 'success',
                title: 'Rút tiền thành công',
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
        url: 'http://localhost:3300/withdraws',
        data: JSON.stringify(obj)
    })
        .done((data) => {

        })
        .fail((error) => {
            console.log(error);
        })
})



function getRecipients(customerId) {
    $.ajax({
        url: 'http://localhost:3300/customers',
        type: 'GET',
        dataType: 'json',
        success: function (customers) {
            var recipientSelect = $('#recipientSelect');
            recipientSelect.empty();
            recipientSelect.append($('<option>').val('').text('Select recipient'));

            $.each(customers, function (index, customer) {
                if (customer.id !== customerId && customer.deleted === 0) {
                    recipientSelect.append($('<option>').val(customer.id).text(customer.id +
                        ' - ' + customer.fullName));
                }
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log('Error: ' + textStatus + ' ' + errorThrown);
        }
    });
}



function handleAddEventShowModalTransfer() {

    $('.transfer').on('click', function () {
        customerId = $(this).data('id');
        getCustomerById(customerId).then((data) => {
                console.log(data);

                if (data !== {}) {
                    $('#senderIdTf').val(data.id);
                    $('#senderNameTf').val(data.fullName);
                    $('#emailTf').val(data.email);
                    $('#balanceTf').val(data.balance);
                    getRecipients(customerId);
                    $('#mdTransfer').modal('show');
                } else {
                    alert('Customer not found');
                }
            })
            .catch((error) => {
                console.log(error);
            });
    })
}



$('#btnTransfer').on('click', () => {

    let recipientId = $('#recipientSelect').val();


    getCustomerById(customerId).then((data) => {
        let customer = data;
        let currentBalance = customer.balance;
        let transactionAmount = +$('#total').val();
        let newBalance = currentBalance - transactionAmount;
        customer.balance = newBalance;

        getCustomerById(recipientId).then((data) => {
            let recipient = data;
            let transferAmount = +$('#transfer').val();
            let currentBalanceRecipient = recipient.balance;
            let newBalanceRecipient = currentBalanceRecipient + transferAmount;
            recipient.balance = newBalanceRecipient;

            let fees = +$('#fees').val();
            let feesAmount = (fees * transferAmount) / 100;


            let transfer = {
                senderId: customer.id,
                recipientId: recipient.id,
                fees: fees,
                feesAmount: feesAmount,
                transferAmount: transferAmount,
                transactionAmount: transactionAmount
            }

            $.ajax({
                    type: 'PATCH',
                    headers: {
                        'accept': 'application/json',
                        'content-type': 'application/json'
                    },
                    url: 'http://localhost:3300/customers/' + recipient.id,
                    data: JSON.stringify(recipient)
                }).done((data) => {
                    let str = renderCustomer(data);
                    $('#tr_' + recipient.id).replaceWith(str);
                })
                .fail((error) => {

                })

                $.ajax({
                    type: 'PATCH',
                    headers: {
                        'accept': 'application/json',
                        'content-type': 'application/json'
                    },
                    url: 'http://localhost:3300/customers/' + customer.id,
                    data: JSON.stringify(customer)
                }).done((data) => {
                    let str = renderCustomer(data);
                    $('#tr_' + customer.id).replaceWith(str);
        
                    $('#transfer').val('');
                    $('#total').val('');
                    $('#mdTransfer').modal('hide');
        
                    handleAddEventShowModalUpdate();
                    handleAddEventShowModalDeposit();
                    handleAddEventShowModalWithdraw();
                    handleAddEventModalDelete();
                    handleAddEventShowModalTransfer();
        
                })

                $.ajax({
                    type: 'POST',
                    headers: {
                        'accept': 'application/json',
                        'content-type': 'application/json'
                    },
                    url: 'http://localhost:3300/transfers',
                    data: JSON.stringify(transfer)
                })   
        })
    })
});



function handleAddEventModalDelete() {
    $('.delete').on('click', function () {
        customerId = $(this).data('id');
        Swal.fire({
            title: 'Are you sure?',
            text: "You won't be able to revert this!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085D6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Yes, delete it!'
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    type: 'PATCH',
                    //tên API
                    url: "http://localhost:3300/customers/" + customerId,
                    //xử lý khi thành công
                    data: {
                        deleted: 1
                    }
                })
                    .done(() => {
                        $('#tr_' + customerId).remove();
                        Swal.fire({
                            position: 'top-end',
                            icon: 'success',
                            title: 'Customer has been deleted',
                            showConfirmButton: false,
                            timer: 1500
                        })
                    })
            }
        })
    })
}

