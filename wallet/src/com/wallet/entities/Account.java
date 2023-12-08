package com.wallet.entities;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Account {
    private int accountId;
    private String accountName;
    private double balance;
    private List<Transaction> transactions;
    private Currency currency;
    private AccountType type;

    public Account(int accountId, String accountName, double balance, List<Transaction> transactions, Currency currency, AccountType type) {
        this.accountId = accountId;
        this.accountName = accountName;
        this.balance = balance;
        this.transactions = transactions;
        this.currency = currency;
        this.type = type;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + accountId +
                ", name='" + accountName + '\'' +
                ", balance=" + balance +
                ", transactions=" + transactions +
                ", currency=" + currency +
                ", type=" + type +
                '}';
    }
    public Account executeTransaction(Transaction transaction) {
        if (getType() == AccountType.BANK || getBalance() >= transaction.getAmount()) {
            Transaction newTransaction = new Transaction(
                    transaction.getTransactionId(),
                    transaction.getlabel(),
                    transaction.getAmount(),
                    transaction.getTransactionDateTime(),
                    transaction.getType()
            );


            getTransactions().add(newTransaction);

            if (transaction.getType() == TransactionType.DEBIT) {
                updateBalance(-transaction.getAmount());
            } else {
                updateBalance(transaction.getAmount());
            }

            return new Account(
                    getAccountId(),
                    getAccountName(),
                    getBalance(),
                    getTransactions(),
                    getCurrency(),
                    getType()
            );
        } else {
            System.out.println("Solde insuffisant pour effectuer la transaction.");
            return this;
        }
    }
    private void updateBalance(double amount) {
        setBalance(getBalance() + amount);
    }
    public double getBalanceAtDateTime(Date dateTime) {
        double balance = 0.0;

        for (Transaction transaction : transactions) {
            if (transaction.getTransactionDateTime().before(dateTime) || transaction.getTransactionDateTime().equals(dateTime)) {
                if (transaction.getType() == TransactionType.CREDIT) {
                    balance += transaction.getAmount();
                } else if (transaction.getType() == TransactionType.DEBIT) {
                    balance -= transaction.getAmount();
                }
            }
        }

        return balance;
    }
    public List<Double> getBalanceHistoryInDateTimeRange(Date startDateTime, Date endDateTime) {
        List<Double> balanceHistory = new ArrayList<>();
        double currentBalance = 0.0;

        for (Transaction transaction : transactions) {
            if ((transaction.getTransactionDateTime().after(startDateTime) || transaction.getTransactionDateTime().equals(startDateTime))
                    && transaction.getTransactionDateTime().before(endDateTime)) {

                if (transaction.getType() == TransactionType.CREDIT) {
                    currentBalance += transaction.getAmount();
                } else if (transaction.getType() == TransactionType.DEBIT) {
                    currentBalance -= transaction.getAmount();
                }

                balanceHistory.add(currentBalance);
            }
        }

        return balanceHistory;
    }
}
