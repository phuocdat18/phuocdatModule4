page.dialogs.elements.frmCreateCustomer.validate({
    rules: {
        fullNameCre: {
            required: true,
            minlength: 5,
            maxlength: 25
        },
        emailCre: {
            required: true
        }
    },
    messages: {
        fullNameCre: {
            required: 'Họ tên là bắt buộc',
            minlength: 'Họ tên tối thiểu là ${0} ký tự',
            maxlength: 'Họ tên tối đa là ${0} ký tự'
        },
        emailCre: {
            required: 'Email là bắt buộc'
        }
    },
    errorLabelContainer: "#modalCreate .error-area",
    errorPlacement: function (error, element) {
        error.appendTo("#modalCreate .error-area");
    },
    showErrors: function(errorMap, errorList) {
        if (this.numberOfInvalids() > 0) {
            page.dialogs.elements.errorCreateArea.removeClass("hide").addClass("show");
        } else {
            page.dialogs.elements.errorCreateArea.removeClass("show").addClass("hide").empty();
            $("#frmCreate input.error").removeClass("error");
        }
        this.defaultShowErrors();
    },
    submitHandler: function () {
        page.dialogs.commands.create();
    }
})


page.dialogs.elements.frmDeposit.validate({
    rules: {
        transactionAmountDep: {
            required: true
        }
    },
    messages: {
        transactionAmountDep: {
            required: 'Số tiền muốn nạp là bắt buộc'
        }
    },
    errorLabelContainer: "#modalDeposit .error-area",
    errorPlacement: function (error, element) {
        error.appendTo("#modalDeposit .error-area");
    },
    showErrors: function(errorMap, errorList) {
        if (this.numberOfInvalids() > 0) {
            page.dialogs.elements.errorDepositArea.removeClass("hide").addClass("show");
        } else {
            page.dialogs.elements.errorDepositArea.removeClass("show").addClass("hide").empty();
            $("#frmDeposit input.error").removeClass("error");
        }
        this.defaultShowErrors();
    },
    submitHandler: function () {
        page.dialogs.commands.deposit();
    }
})


page.dialogs.elements.frmWithdraw.validate({
    rules: {
        transactionAmountWd: {
            required: true
        }
    },
    messages: {
        transactionAmountWd: {
            required: 'Số tiền muốn nạp là bắt buộc'
        }
    },
    errorLabelContainer: "#modalWithdraw .error-area",
    errorPlacement: function (error, element) {
        error.appendTo("#modalWithdraw .error-area");
    },
    showErrors: function(errorMap, errorList) {
        if (this.numberOfInvalids() > 0) {
            page.dialogs.elements.errorWithdrawArea.removeClass("hide").addClass("show");
        } else {
            page.dialogs.elements.errorWithdrawArea.removeClass("show").addClass("hide").empty();
            $("#frmWithdraw input.error").removeClass("error");
        }
        this.defaultShowErrors();
    },
    submitHandler: function () {
        page.dialogs.commands.withdraw();
    }
})


page.dialogs.elements.frmTransfer.validate({
    rules: {
        transferAmountTrf: {
            required: true
        }
    },
    messages: {
        transferAmountTrf: {
            required: 'Số tiền muốn chuyển là bắt buộc'
        }
    },
    errorLabelContainer: "#modalTransfer .error-area",
    errorPlacement: function (error, element) {
        error.appendTo("#modalTransfer .error-area");
    },
    showErrors: function(errorMap, errorList) {
        if (this.numberOfInvalids() > 0) {
            page.dialogs.elements.errorAreaTransfer.removeClass("hide").addClass("show");
        } else {
            page.dialogs.elements.errorAreaTransfer.removeClass("show").addClass("hide").empty();
            $("#frmTransfer input.error").removeClass("error");
        }
        this.defaultShowErrors();
    },
    submitHandler: function () {
        page.dialogs.commands.transfer();
    }
})