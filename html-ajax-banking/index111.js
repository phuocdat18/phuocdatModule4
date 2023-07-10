const page = {
    url: {
        getAllCustomers: App.API_CUSTOMER + '?delete=0',
        getCustomerById: App.API_CUSTOMER,
        createCustomer: App.API_CUSTOMER,
        updateCustomer: App.API_CUSTOMER,
        incrementBalance: App.API_CUSTOMER,
        deposit: App.API_DEPOSIT,
        withdraw: App.API_WITHDRAW
    },
    elements: {},
    loadData: {},
    commands: {},
    dialogs: {
        elements: {},
        commands: {}
    },
    initializeControlEvent: {}
}

let customerId = 0;
let customer = new Customer();
let deposit = new Deposit();
let withdraw = new Withdraw();

page.elements.btnShowCreateModal = $('#btnShowCreateModal');
page.elements.tbCustomerBody = $('#tbCustomer tbody');

page.dialogs.elements.modalCreate = $('#modalCreate');
page.dialogs.elements.frmCreate = $('#frmCreate');
page.dialogs.elements.fullNameCre = $('#fullNameCre');
page.dialogs.elements.emailCre = $('#emailCre');
page.dialogs.elements.phoneCre = $('#phoneCre');
page.dialogs.elements.addressCre = $('#addressCre');
page.dialogs.elements.btnCreate = $('#btnCreate');

page.dialogs.elements.modalUpdate = $('#modalUpdate');
page.dialogs.elements.fullNameUp = $('#fullNameUp');
page.dialogs.elements.emailUp = $('#emailUp');
page.dialogs.elements.phoneUp = $('#phoneUp');
page.dialogs.elements.addressUp = $('#addressUp');
page.dialogs.elements.btnUpdate = $('#btnUpdate');

page.dialogs.elements.modalDeposit = $('#modalDeposit');
page.dialogs.elements.frmDeposit = $('#frmDeposit');
page.dialogs.elements.idDeposit = $('#idDeposit');
page.dialogs.elements.fullNameDeposit = $('#fullNameDeposit');
page.dialogs.elements.emailDeposit = $('#emailDeposit');
page.dialogs.elements.balanceDeposit = $('#balanceDeposit');
page.dialogs.elements.transactionAmountDeposit = $('#transactionAmountDeposit');
page.dialogs.elements.btnDeposit = $('#btnDeposit');

page.dialogs.elements.modalWithdraw = $('#modalWithdraw');
page.dialogs.elements.frmWithdraw = $('#frmWithdraw');
page.dialogs.elements.idWithdraw = $('#idWithdraw');
page.dialogs.elements.fullNameWithdraw = $('#fullNameWithdraw');
page.dialogs.elements.emailWithdraw = $('#emailWithdraw');
page.dialogs.elements.balanceWithdraw = $('#balanceWithdraw');
page.dialogs.elements.transactionAmountWithdraw = $('#transactionAmountWithdraw');
page.dialogs.elements.btnWithdraw = $('#btnWithdraw');

page.commands.renderCustomer = (obj) => {
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

function addAllEvent() {
    page.commands.handleAddEventShowModalUpdate
    page.commands.handleAddEventShowModalDeposit
    page.commands.handleAddEventShowModalWithdraw
    page.commands.handleAddEventModalDelete
    handleAddEventShowModalTransfer();
    page.dialogs.commands.create
}


page.commands.getAllCustomers = () => {
    page.elements.tbCustomerBody.empty();
    $.ajax({
            type: 'GET',
            url: page.url.getAllCustomers
        })
        .done((data) => {
            data.forEach(item => {
                const str = page.commands.renderCustomer(item);
                page.elements.tbCustomerBody.prepend(str);

            });
            addAllEvent();
        })
        .fail((error) => {
            console.log(error);
        })
    let createDiv = $("#create-result");
    createDiv.innerHTML = "";

    let updateDiv = $("#update-result");
    updateDiv.innerHTML = "";

    let depositDiv = $("#deposit-result");
    depositDiv.innerHTML = "";

    let withdrawDiv = $("#withdraw-result");
    withdrawDiv.innerHTML = "";

    let transferDiv = $("#transfer-result");
    transferDiv.innerHTML = "";
}

page.commands.getCustomerById = (id) => {
    return $.ajax({
        type: 'get',
        url: page.url.getCustomerById + '/' + id,
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



page.commands.handleAddEventShowModalUpdate = (customerId) => {
    page.commands.getCustomerById(customerId).then((data) => {
            page.dialogs.elements.fullNameUp.val(data.fullName);
            page.dialogs.elements.emailUp.val(data.email);
            page.dialogs.elements.phoneUp.val(data.phone);
            page.dialogs.elements.addressUp.val(data.address);

            page.dialogs.elements.modalUpdate.modal('show');
        })
        .catch((error) => {
            console.log(error);
        });
}


page.commands.handleAddEventShowModalDeposit = (customerId) => {
    page.commands.getCustomerById(customerId).then((data) => {
            customer = data;
            page.dialogs.elements.idDeposit.val(customer.id);
            page.dialogs.elements.fullNameDeposit.val(customer.fullName);
            page.dialogs.elements.emailDeposit.val(customer.email);
            page.dialogs.elements.balanceDeposit.val(customer.balance);

            page.dialogs.elements.modalDeposit.modal('show');
        })
        .catch((error) => {
            console.log(error);
        });
}

page.commands.handleAddEventShowModalWithdraw = (customerId) => {
    page.commands.getCustomerById(customerId).then((data) => {
            customer = data;
            page.dialogs.elements.idWithdraw.val(customer.id);
            page.dialogs.elements.fullNameWithdraw.val(customer.fullName);
            page.dialogs.elements.emailWithdraw.val(customer.email);
            page.dialogs.elements.balanceWithdraw.val(customer.balance);

            page.dialogs.elements.modalWithdraw.modal('show');
        })
        .catch((error) => {
            console.log(error);
        });
}

page.dialogs.commands.create = () => {
    const fullName = page.dialogs.elements.fullNameCre.val();
    const email = page.dialogs.elements.emailCre.val();
    const phone = page.dialogs.elements.phoneCre.val();
    const address = page.dialogs.elements.addressCre.val();
    const balance = 0;
    const deleted = 0;

    let requires = [];
    if (fullName == "") requires.push("Tên không được để trống");
    if (email == "") requires.push("Email không được để trống");
    if (address == "") requires.push("Địa chỉ không được để trống");
    if (phone == "") requires.push("Phone không được để trống");

    let createDiv = $("#create-result");
    createDiv.empty();

    if (requires.length > 0) {
        var resultStr = ""
        for (var i = 0; i < requires.length; i++) {
            resultStr += `
            <p class="alert alert-danger">${requires[i]}</p>
            `;
        }
        createDiv.html(resultStr)
    } else {
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
                    'accept': 'application/json',
                    'content-type': 'application/json'
                },
                type: 'POST',
                url: page.url.createCustomer,
                data: JSON.stringify(obj)
            })
            .done((data) => {
                const str = page.commands.renderCustomer(data);
                page.elements.tbCustomerBody.prepend(str);

                addAllEvent();
                page.dialogs.elements.modalCreate.modal('hide');
                Swal.fire({
                    position: 'top-end',
                    icon: 'success',
                    title: 'Thêm mới thành công',
                    showConfirmButton: false,
                    timer: 1500
                })
            })
            .fail((error) => {
                console.log(error);
            })
    }
}

page.dialogs.commands.update = (obj) => {
    $.ajax({
            headers: {
                'accept': 'application/json',
                'content-type': 'application/json'
            },
            type: 'PATCH',
            url: page.url.updateCustomer + '/' + customerId,
            data: JSON.stringify(obj)
        })
        .done((data) => {
            const str = page.commands.renderCustomer(data);

            const currentRow = $('#tr_' + customerId);
            currentRow.replaceWith(str);

            page.dialogs.elements.modalUpdate.modal('hide');
        })
        .fail((error) => {
            console.log(error);
        })
}


page.dialogs.commands.deposit = (customer, deposit) => {
    $.ajax({
            headers: {
                'accept': 'application/json',
                'content-type': 'application/json'
            },
            type: 'PATCH',
            url: page.url.incrementBalance + '/' + customerId,
            data: JSON.stringify(customer)
        })
        .done((data) => {
            const str = page.commands.renderCustomer(data);

            const currentRow = $('#tr_' + customerId);
            currentRow.replaceWith(str);

            page.dialogs.elements.modalDeposit.modal('hide');

            App.showSuccessAlert('Nạp tiền thành công');
        })
        .fail((error) => {
            console.log(error);
        })

    $.ajax({
            headers: {
                'accept': 'application/json',
                'content-type': 'application/json'
            },
            type: 'POST',
            url: page.url.deposit,
            data: JSON.stringify(deposit)
        })
        .done((data) => {

        })
        .fail((error) => {
            console.log(error);
        })
}







page.dialogs.commands.withdraw = (customer, withdraw) => {
    $.ajax({
            headers: {
                'accept': 'application/json',
                'content-type': 'application/json'
            },
            type: 'PATCH',
            url: page.url.incrementBalance + '/' + customerId,
            data: JSON.stringify(customer)
        })
        .done((data) => {
            const str = page.commands.renderCustomer(data);

            const currentRow = $('#tr_' + customerId);
            currentRow.replaceWith(str);

            page.dialogs.elements.modalWithdraw.modal('hide');

            App.showSuccessAlert('Rút tiền thành công');
        })
        .fail((error) => {
            console.log(error);
        })

    $.ajax({
            headers: {
                'accept': 'application/json',
                'content-type': 'application/json'
            },
            type: 'POST',
            url: page.url.withdraw,
            data: JSON.stringify(withdraw)
        })
        .done((data) => {

        })
        .fail((error) => {
            console.log(error);
        })
}


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

document.addEventListener("input", () => {
    let fee = Number(document.getElementById("fees").value);
    let transferAmount = Number(document.getElementById("transfer").value);
    let transactionFee = transferAmount * fee / 100;
    document.getElementById("total").value = Math.round(transactionFee + transferAmount);
})

function handleAddEventShowModalTransfer() {
    $('.transfer').on('click', function () {
        customerId = $(this).data('id');
        page.commands.getCustomerById(customerId).then((data) => {
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

    page.commands.getCustomerById(customerId).then((senderData) => {
        const transferAmount = +$('#transfer').val();
        let sender = senderData;
        const currentBalance = sender.balance;


        let requires = [];
        if (transferAmount == "") requires.push("Số tiền không được để trống");
        if (transferAmount < 1) requires.push("Số tiền phải lớn hơn 0");
        if (transferAmount > currentBalance) requires.push("Số tiền phải bé hơn số tiền trong tài khoản người gửi");

        let transferDiv = $("#transfer-result");
        transferDiv.empty();
        if (requires.length > 0) {
            var resultStr = ""
            for (var i = 0; i < requires.length; i++) {
                resultStr += `
                <p class="alert alert-danger">${requires[i]}</p>
                `;
            }
            transferDiv.html(resultStr)
        } else {
            let recipientId = $('#recipientSelect').val();
            let transactionAmount = +$('#total').val();
            let newBalance = currentBalance - transactionAmount;
            sender.balance = newBalance;

            page.commands.getCustomerById(recipientId).then((recipientData) => {
                let recipient = recipientData;
                let transferAmount = +$('#transfer').val();
                let currentBalanceRecipient = recipient.balance;
                let newBalanceRecipient = currentBalanceRecipient + transferAmount;
                recipient.balance = newBalanceRecipient;

                let fees = +$('#fees').val();
                let feesAmount = (fees * transferAmount) / 100;

                let transfer = {
                    senderId: sender.id,
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
                    data: JSON.stringify(recipient),
                    success: (data) => {
                        let str = page.commands.renderCustomer(data);
                        $('#tr_' + recipient.id).replaceWith(str);
                    },
                    error: (error) => {
                        console.log(error);
                    }
                });

                $.ajax({
                    type: 'PATCH',
                    headers: {
                        'accept': 'application/json',
                        'content-type': 'application/json'
                    },
                    url: 'http://localhost:3300/customers/' + sender.id,
                    data: JSON.stringify(sender),
                    success: (data) => {
                        let str = page.commands.renderCustomer(data);
                        $('#tr_' + sender.id).replaceWith(str);

                        // $('#transfer').val('');
                        // $('#total').val('');

                        page.dialogs.elements.frmTransfer[0].reset();
                        $('#transfer-result').empty();
                        $('#mdTransfer').modal('hide');

                        addAllEvent();
                    },
                    error: (error) => {
                        console.log(error);
                    }
                });

                $.ajax({
                    type: 'POST',
                    headers: {
                        'accept': 'application/json',
                        'content-type': 'application/json'
                    },
                    url: 'http://localhost:3300/transfers',
                    data: JSON.stringify(transfer),
                    success: () => {
                        // Handle success if needed
                    },
                    error: (error) => {
                        console.log(error);
                    }
                });
            });
        }
    });
});

page.commands.handleAddEventModalDelete = (customerId) => {
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
                    url: page.url.updateCustomer + '/' + customerId,
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
}


page.initializeControlEvent = () => {
    page.elements.btnShowCreateModal.on('click', () => {
        page.dialogs.elements.modalCreate.modal('show');
    })

    page.dialogs.elements.btnCreate.on('click', () => {
        page.dialogs.commands.create();
    })

    page.elements.tbCustomerBody.on('click', '.edit', function () {
        customerId = $(this).data('id');
        page.commands.handleAddEventShowModalUpdate(customerId);
    })
    

    page.dialogs.commands.closeModalCreate = () => {
        page.dialogs.elements.frmCreate[0].reset();
        $('#create-result').empty();
    }

    page.dialogs.elements.modalCreate.on("hidden.bs.modal", function () {
        page.dialogs.commands.closeModalCreate();
    });

    page.dialogs.commands.closeModalDeposit = () => {
        page.dialogs.elements.frmDeposit[0].reset();
        $('#deposit-result').empty();
    }

    page.dialogs.elements.modalDeposit.on("hidden.bs.modal", function () {
        page.dialogs.commands.closeModalDeposit();
    })

    page.dialogs.commands.closeModalWithdraw = () => {
        page.dialogs.elements.frmWithdraw[0].reset();
        $('#withdraw-result').empty();
    }

    page.dialogs.elements.modalWithdraw.on("hidden.bs.modal", function () {
        page.dialogs.commands.closeModalWithdraw();
    })

    page.elements.tbCustomerBody.on('click', '.deposit', function () {
        customerId = $(this).data('id');
        page.commands.handleAddEventShowModalDeposit(customerId);
    })

    page.elements.tbCustomerBody.on('click', '.withdraw', function () {
        customerId = $(this).data('id');
        page.commands.handleAddEventShowModalWithdraw(customerId);
    })

    page.elements.tbCustomerBody.on('click', '.delete', function () {
        customerId = $(this).data('id');
        page.commands.handleAddEventModalDelete(customerId);
    })

    page.dialogs.elements.btnUpdate.on('click', () => {
        const fullName = page.dialogs.elements.fullNameUp.val();
        const email = page.dialogs.elements.emailUp.val();
        const phone = page.dialogs.elements.phoneUp.val();
        const address = page.dialogs.elements.addressUp.val();

        delete customer.id;

        let requires = [];
        if (fullName == "") requires.push("Tên không được để trống");
        if (email == "") requires.push("Email không được để trống");
        if (address == "") requires.push("Địa chỉ không được để trống");
        if (phone == "") requires.push("Phone không được để trống");

        let updateDiv = $("#update-result");
        updateDiv.empty();

        if (requires.length > 0) {
            var resultStr = ""
            for (var i = 0; i < requires.length; i++) {
                resultStr += `
                <p class="alert alert-danger">${requires[i]}</p>
                `;
            }
            updateDiv.html(resultStr)
        } else {
            customer.fullName = fullName;
            customer.email = email;
            customer.phone = phone;
            customer.address = address;

            page.dialogs.commands.update(customer);
        }
    })

    page.dialogs.elements.btnDeposit.on('click', () => {
        const transactionAmount = +page.dialogs.elements.transactionAmountDeposit.val();

        let requires = [];
        if (transactionAmount == "") requires.push("Số tiền không được để trống");
        if (transactionAmount < 1) requires.push("Số tiền nạp phải lớn hơn 0");

        let depositDiv = $("#deposit-result");
        depositDiv.empty();

        if (requires.length > 0) {
            var resultStr = ""
            for (var i = 0; i < requires.length; i++) {
                resultStr += `
                <p class="alert alert-danger">${requires[i]}</p>
                `;
            }
            depositDiv.html(resultStr)
        } else {
            const currentBalance = customer.balance;
            const newBalance = currentBalance + transactionAmount;
            customer.balance = newBalance;

            const deposit = {
                id: null,
                customerId: customerId,
                transactionAmount: transactionAmount
            };

            page.dialogs.commands.deposit(customer, deposit);
        }
    });


    page.dialogs.elements.btnWithdraw.on('click', () => {
        const transactionAmount = +page.dialogs.elements.transactionAmountWithdraw.val();
        const currentBalance = customer.balance;

        let requires = [];
        if (transactionAmount == "") requires.push("Số tiền không được để trống");
        if (transactionAmount < 1) requires.push("Số tiền rút phải lớn hơn 0");
        if (transactionAmount > currentBalance) requires.push("Số tiền rút phải bé hơn Số tiền có trong tài khoản");

        let withdrawDiv = $("#withdraw-result");
        withdrawDiv.empty();

        if (requires.length > 0) {
            var resultStr = ""
            for (var i = 0; i < requires.length; i++) {
                resultStr += `
                <p class="alert alert-danger">${requires[i]}</p>
                `;
            }
            withdrawDiv.html(resultStr)
        } else {
            const transactionAmount = +$('#transactionAmountWithdraw').val();
            const newBalance = currentBalance - transactionAmount;
            customer.balance = newBalance;

            withdraw.id = null;
            withdraw.customerId = customerId;
            withdraw.transactionAmount = transactionAmount;

            page.dialogs.commands.withdraw(customer, withdraw);
        }
    })

}

page.loadData = () => {
    page.commands.getAllCustomers();
}

$(() => {
    page.loadData();

    page.initializeControlEvent();
})