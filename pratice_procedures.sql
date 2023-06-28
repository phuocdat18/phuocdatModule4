DELIMITER //
CREATE PROCEDURE `DepositMoney`(
    IN customer_id BIGINT,
    IN transaction_amount DECIMAL(10, 2)
)
BEGIN
    UPDATE `customers`
    SET balance = balance + transaction_amount,
        updated_at = NOW()
    WHERE id = customer_id;
    
    INSERT INTO `deposits` (created_at, created_by, customer_id, transaction_amount)
    VALUES (NOW(), 'user', customer_id, transaction_amount);
    
    SELECT * FROM customers WHERE id = customer_id;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE WithdrawMoney(
    IN customer_id BIGINT,
    IN transaction_amount DECIMAL(10, 2)
)
BEGIN
    DECLARE current_balance DECIMAL(10, 2);
    
    SELECT balance INTO current_balance FROM customer WHERE id = customer_id;
    
    IF current_balance >= transaction_amount THEN
        UPDATE customer
        SET balance = balance - transaction_amount,
            updated_at = NOW()
        WHERE id = customer_id;
        
        INSERT INTO withdraws (created_at, created_by, customer_id, transaction_amount)
        VALUES (NOW(), 'user', customer_id, transaction_amount);
        
        SELECT * FROM customers WHERE id = customer_id;
    ELSE
        SELECT 'Insufficient balance' AS error_message;
    END IF;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE TransferMoney(
    IN sender_id INT,
    IN recipient_id INT,
    IN transaction_amount DECIMAL(10, 2)
)
BEGIN
    DECLARE sender_balance DECIMAL(10, 2);
    DECLARE transaction_fee DECIMAL(10, 2);
    
    SELECT balance INTO sender_balance FROM customer WHERE id = sender_id;
    SET transaction_fee = transaction_amount * 0.1;
    
    IF sender_balance >= transaction_amount + transaction_fee THEN
        UPDATE customer
        SET balance = balance - (transaction_amount + transaction_fee),
            updated_at = NOW()
        WHERE id = sender_id;
        
        UPDATE customer
        SET balance = balance + transaction_amount,
            updated_at = NOW()
        WHERE id = recipient_id;
        
        INSERT INTO transfers (created_at, created_by, sender_id, recipient_id, fees, fees_amount, transaction_amount, transfer_amount)
        VALUES (NOW(), 'user', sender_id, recipient_id, 'Bank fee', transaction_fee, transaction_amount, transaction_amount + transaction_fee);
        
        SELECT * FROM customer WHERE id = sender_id;
    ELSE
        SELECT 'Insufficient balance' AS error_message;
    END IF;
END //
DELIMITER ;


CREATE VIEW transfer_history AS
SELECT t.id AS transfer_id, 
       c.id AS sender_id, 
       c.full_name AS sender_name, 
       r.id AS recipient_id, 
       r.full_name AS recipient_name,
       t.created_at, 
       t.fees, 
       t.fees_amount, 
       t.transaction_amount, 
       t.transfer_amount
FROM transfers t
JOIN customers c ON t.sender_id = c.id
JOIN customers r ON t.recipient_id = r.id;


CREATE VIEW deposits_history AS
SELECT c.id AS customer_id, 
       c.full_name AS customer_name, 
       d.transaction_amount AS transaction_amount, 
       c.balance AS balance
FROM deposits d
JOIN customers c ON d.customer_id = c.id;


CREATE VIEW withdraws_history AS
SELECT c.id AS customer_id, 
       c.full_name AS customer_name, 
       w.transaction_amount AS transaction_amount, 
       c.balance AS balance
FROM withdraws w
JOIN customers c ON w.customer_id = c.id;



