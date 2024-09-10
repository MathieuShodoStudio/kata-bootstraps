export interface Calendar {
  now(): string
}

enum OperationType {
  WITHDRAWAL,
  DEPOSIT
}

interface Operation {
  type: OperationType
  date: string
  amount: number
  currentBalance: number
}

type StatementFormat = 'CSV' | 'HTML'

export interface AccountPrinter {
  printStatement(account: Account): string
}

export class AccountPrinterCsv implements AccountPrinter {
  public printStatement(account: Account): string {
    if (account.operations.length < 1) {
      return ''
    }

    let statement = 'Date,Amount,Balance'
    for (const operation of account.operations) {
      if (operation.type === OperationType.DEPOSIT) {
        statement += `\n${operation.date},+${operation.amount},${operation.currentBalance}`
      } else {
        statement += `\n${operation.date},-${operation.amount},${operation.currentBalance}`
      }
    }

    return statement
  }
}

class AccountPrinterHtml implements AccountPrinter {
  public printStatement(account: Account): string {
    if (account.operations.length < 1) {
      return ''
    }

    let statement = `<table>
    <thead>
    <tr>
        <th>Date</th>
        <th>Amount</th>
        <th>Balance</th>
    </tr>
    </thead>
    <tbody>
`

    for (const operation of account.operations) {
      statement += `    <tr>
        <td>${operation.date}</td>
        <td>${operation.type === OperationType.DEPOSIT ? '+' : '-'}${operation.amount}</td>
        <td>${operation.currentBalance}</td>
    </tr>
`
    }

    statement += `    </tbody>
</table>`

    return statement
  }
}

export class Account {
  public readonly operations: Operation[] = []
  private currentBalance = 0

  constructor(readonly calendar: Calendar) {
  }

  public printStatement(format: StatementFormat = 'CSV'): string {
    const printer: AccountPrinter = format === 'CSV' ? new AccountPrinterCsv() : new AccountPrinterHtml()

    return printer.printStatement(this)
  }

  public deposit(amount: number): void {
    const now = this.calendar.now()
    this.currentBalance += amount
    this.operations.push({type: OperationType.DEPOSIT, date: now, amount, currentBalance: this.currentBalance })
  }

  public withdraw(amount: number): void {
    if (amount > this.currentBalance) {
      throw new Error('Overdraft')
    }

    const now = this.calendar.now()
    this.currentBalance -= amount
    this.operations.push({type: OperationType.WITHDRAWAL, date: now, amount, currentBalance: this.currentBalance })
  }
}
