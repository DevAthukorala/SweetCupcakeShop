# Sweet Cupcake Shop

A Java application for managing a cupcake catalog and cashier accounts. It includes a Swing desktop interface and an alternative console interface, with data saved locally in CSV files.

## Features

| Feature | Manager | Cashier |
| --- | --- | --- |
| View cupcakes and prices | Yes | Yes |
| Add cupcakes | Yes | Yes |
| Search by name or category | Yes | Yes |
| Create cashier accounts | Yes | No |

Search supports case-insensitive partial matches. The desktop interface offers Classic, Seasonal, Custom Order, Gluten-Free, and Special Collection categories.

## Requirements

- JDK 17 or later, with `java` and `javac` on your PATH.
- A graphical desktop to use the Swing interface.
- Optional: NetBeans for opening the included IDE project, or Apache Ant for the supplied build script.

No external Java libraries or database server are required.

## Get started

```sh
git clone https://github.com/DevAthukorala/SweetCupcakeShop.git
cd SweetCupcakeShop
```

Run all commands below from the project root. The application resolves its `data/` directory relative to the current working directory.

### Build and run with the JDK

```sh
javac -encoding UTF-8 --release 17 -d build/classes src/sweetcupcakeshop/*.java
java -cp build/classes sweetcupcakeshop.LoginForm
```

To use the console interface after compiling:

```sh
java -cp build/classes sweetcupcakeshop.SweetCupcakeShop
```

### Run with NetBeans

1. Choose **File > Open Project** and select the cloned folder.
2. Configure the project's Java platform to use JDK 17 or later.
3. Choose **Clean and Build**, then **Run Project**.

The configured main class is `sweetcupcakeshop.LoginForm`.

### Build with Ant

```sh
ant clean jar
java -jar dist/SweetCupcakeShop.jar
```

A prebuilt JAR is also included in `CupCakeShop App/`. To try it from the project root:

```sh
java -jar "CupCakeShop App/SweetCupcakeShop.jar"
```

Build from source when you need a JAR that reflects source changes.

## First login

When `data/users.csv` does not exist, the application creates it with this default manager account:

| Username | Password | Role |
| --- | --- | --- |
| `admin` | `admin123` | Manager |

After signing in:

1. Open **View Cupcakes** and click **Refresh** to populate the table.
2. Use **Add Cupcake** to enter a name, category, and numeric price.
3. Use the search tab to find cupcakes by name or category.
4. As a manager, use **Create Cashier** to add a cashier login.
5. Click **Logout** to return to the login screen.

Accounts are stored in plain text, and the default credentials are intended for local demonstration. This version does not provide production-grade authentication.

## Data storage

| File | Contents |
| --- | --- |
| `data/cupcakes.csv` | Cupcake catalog with `id,name,category,price` columns; includes sample records |
| `data/users.csv` | Local accounts with `username,password,role` columns; created automatically and excluded from Git |

New cupcakes receive an automatically assigned ID. Adding cupcakes and creating cashier accounts saves changes to the corresponding CSV file. Back up the `data/` folder to preserve local records.

## Project structure

```text
SweetCupcakeShop/
|-- src/sweetcupcakeshop/   # Java source files
|-- data/                  # CSV catalog and local accounts
|-- nbproject/             # NetBeans and Ant project configuration
|-- CupCakeShop App/       # Prebuilt JAR and packaging notes
|-- build.xml              # Ant build entry point
|-- manifest.mf            # JAR manifest template
`-- .gitignore             # Excludes build output and local settings/accounts
```

`LoginForm` handles desktop login, `ManagerDashboard` and `CashierDashboard` provide the role-specific screens, `Inventory` manages cupcake persistence and search, and `UserManager` manages accounts. `SweetCupcakeShop` is the console entry point.

## Troubleshooting

- **`java` or `javac` is not recognized:** Install a JDK and add its `bin` directory to your PATH.
- **Unsupported class version:** Check `java -version` and use JDK 17 or later for source builds. If the bundled JAR requires a newer runtime, rebuild from source with the commands above.
- **Cupcake table appears empty:** Click **Refresh** in **View Cupcakes**.
- **Different or missing data:** Confirm you launched the app from the project root; launching elsewhere uses a different relative `data/` directory.
- **Default login fails:** The default account is created only when `data/users.csv` is absent. If that file already exists, use an account stored in it.
