import {Account, Calendar} from './BankingKata'

class FixedCalendar implements Calendar {
  private fixedDate = '10.09.2024'

  public now(): string {
    return this.fixedDate
  }

  public at(date: string) {
    this.fixedDate = date
  }
}

function newAccount() {
  return new AccountDsl()
}

class AccountDsl {
  private calendarMock = new FixedCalendar()
  private readonly account: Account

  constructor() {
    this.account = new Account(this.calendarMock)
  }

  public deposit(amount: number, date: string = '10.09.2024'): AccountDsl {
    this.calendarMock.at(date)
    this.account.deposit(amount)
    return this
  }

  public withdraw(amount: number, date: string = '10.09.2024'): AccountDsl {
    this.calendarMock.at(date)
    this.account.withdraw(amount)
    return this
  }

  public build(): Account {
    return this.account
  }
}

describe('BankingKata', () => {
  it('A new account prints an empty statement', () => {
    const account = newAccount().build()

    const statement = account.printStatement()

    expect(statement).toBe('')
  })

  it('After a deposit of 500 prints a +500 statement', () => {
    const account = newAccount().deposit(500).build()

    const statement = account.printStatement()

    expect(statement).toBe('Date,Amount,Balance\n10.09.2024,+500,500')
  })

  it('After a deposit of 300 yesterday and a deposit of 500 today prints a +300 and +500 statement', () => {
    const account = newAccount()
        .deposit(300, '09.09.2024')
        .deposit(500, '10.09.2024')
        .build()

    const statement = account.printStatement()

    expect(statement).toBe('Date,Amount,Balance\n09.09.2024,+300,300\n10.09.2024,+500,800')
  })

  it('Print a +300 and -200 statement in HTML', () => {
    const account = newAccount()
        .deposit(300, '09.09.2024')
        .withdraw(200, '10.09.2024')
        .build()

    const statement = account.printStatement('HTML')

    expect(statement).toBe(`<table>
    <thead>
    <tr>
        <th>Date</th>
        <th>Amount</th>
        <th>Balance</th>
    </tr>
    </thead>
    <tbody>
    <tr>
        <td>09.09.2024</td>
        <td>+300</td>
        <td>300</td>
    </tr>
    <tr>
        <td>10.09.2024</td>
        <td>-200</td>
        <td>100</td>
    </tr>
    </tbody>
</table>`)
  })

  it('After a deposit of 200 yesterday and a withdraw of 100 today prints a +200 and -100 statement', () => {
    const account = newAccount()
        .deposit(200, '09.09.2024')
        .withdraw(100, '10.09.2024')
        .build()

    const statement = account.printStatement()

    expect(statement).toBe('Date,Amount,Balance\n09.09.2024,+200,200\n10.09.2024,-100,100')
  })

  it('Deny a withdraw of 100 for an empty account', () => {
    const account = newAccount().build()

    expect(() => account.withdraw(100)).toThrow('Overdraft')
  })
})
