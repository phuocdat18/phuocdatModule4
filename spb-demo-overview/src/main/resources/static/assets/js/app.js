class App {
  static DOMAIN_SERVER = window.origin;
  static API_SERVER = this.DOMAIN_SERVER + '/api';

  static API_CUSTOMER = this.API_SERVER + '/customers'
  static API_CREATE_CUSTOMER = this.API_CUSTOMER + '/create'
  static API_UPDATE_CUSTOMER = this.API_CUSTOMER + '/edit'
  static API_DELETE_CUSTOMER = this.API_CUSTOMER + '/delete'

  static API_DEPOSIT = this.API_CUSTOMER + '/deposits'
  static API_WITHDRAW = this.API_CUSTOMER + '/withdraws'
  static API_TRANSFER = this.API_CUSTOMER + '/transfers'

  static API_HISTORY_DEPOSIT = this.API_CUSTOMER + '/historyDeposits'
  static API_HISTORY_WITHDRAW = this.API_CUSTOMER + '/historyWithdraws'
  static API_HISTORY_TRANSFER = this.API_CUSTOMER + '/historyTransfers'


  static showDeleteConfirmDialog() {
    return Swal.fire({
      icon: 'warning',
      text: 'Are you sure you want to delete the selected data ?',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Yes, delete it !',
      cancelButtonText: 'Cancel',
    });
  }

  static showSuccessAlert(t) {
    Swal.fire({
      icon: 'success',
      title: t,
      position: 'top-end',
      showConfirmButton: false,
      timer: 1500,
    });
  }

  static showErrorAlert(t) {
    Swal.fire({
      icon: 'error',
      title: 'Warning',
      text: t,
    });
  }
}

class Customer {
  constructor(id, fullName, email, phone, balance, address) {
    this.id = id;
    this.fullName = fullName;
    this.email = email;
    this.phone = phone;
    this.balance = balance;
    this.address = address;
  }
}

class Deposit {
  constructor(id, customerId, transactionAmount) {
    this.id = id;
    this.customerId = customerId;
    this.transactionAmount = transactionAmount;
  }
}

class Withdraw {
  constructor(id, customerId, transactionAmount) {
    this.id = id;
    this.customerId = customerId;
    this.transactionAmount = transactionAmount;
  }
}

class Transfer {
  constructor(id, senderId, recipientId, transferAmount, transactionAmount, feeAmount){
    this.id = id;
    this.senderId = senderId,
        this.recipientId = recipientId,
        this.transferAmount = transferAmount,
        this.transactionAmount = transactionAmount,
        this.feeAmount = feeAmount
  }
}
// $(function() {
//     $(".num-space").number(true, 0, ',', ' ');
//     $(".num-point").number(true, 0, ',', '.');
//     $(".num-comma").number(true, 0, ',', ',');

//     $('[data-toggle="tooltip"]').tooltip();
// });
