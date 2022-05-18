/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.persistentExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest{

    private static ExpenseManager expenseManager;


    @Before
    public void setup() throws ExpenseManagerException {
        Context context = ApplicationProvider.getApplicationContext();
        expenseManager = new persistentExpenseManager(context);
    }

    @Test
    public void testTransactionLog(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022, 05, 17);
        Date transactionDate = calendar.getTime();

        Random rand = new Random();
        String randAccountNo = String.valueOf(rand.nextInt(10000));

        expenseManager.getTransactionsDAO().logTransaction(transactionDate,randAccountNo,ExpenseType.INCOME,20000.0);

        List<Transaction> transactionList=expenseManager.getTransactionLogs();

        for (Transaction t:transactionList) {
            if(t.getAccountNo().equals(randAccountNo)){
                Assert.assertEquals(t.getAccountNo(),randAccountNo);
                Assert.assertEquals(t.getExpenseType(),ExpenseType.INCOME);
                Assert.assertEquals(t.getAmount(),20000.0,0.0);
            }
        }
    }




    @Test
    public void testAddAccount(){

        List<String> accountNumbers = expenseManager.getAccountNumbersList();

        Random rand = new Random();
        String randAccountNo = String.valueOf(rand.nextInt(10000));

        // Lets confirm that the account number randAccountNo is not in the database
        Assert.assertFalse(accountNumbers.contains(randAccountNo));

        // adding the randAccountNo to the database
        expenseManager.addAccount(
                randAccountNo,
                "Sampath Bank",
                "Amal",
                99999
        );

        // checking account is successfully added

        accountNumbers = expenseManager.getAccountNumbersList();

        Assert.assertTrue(accountNumbers.contains(randAccountNo));


    }


}