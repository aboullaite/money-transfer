package me.aboullaite.moneytransfer;

import me.aboullaite.moneytransfer.exceptions.NotFoundException;
import me.aboullaite.moneytransfer.interfaces.Account;
import me.aboullaite.moneytransfer.interfaces.Base;
import me.aboullaite.moneytransfer.interfaces.Holder;
import me.aboullaite.moneytransfer.interfaces.Transaction;
import me.aboullaite.moneytransfer.interfaces.repositories.AccountsRepository;
import me.aboullaite.moneytransfer.interfaces.repositories.Repository;
import me.aboullaite.moneytransfer.interfaces.repositories.TransactionRepository;
import me.aboullaite.moneytransfer.service.TransferService;
import me.aboullaite.moneytransfer.utils.JsonUtils;
import me.aboullaite.moneytransfer.utils.PaginationParams;
import me.aboullaite.moneytransfer.utils.TransactionPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import static spark.Spark.*;

import javax.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;


final class MoneyTransfer {

    private static final String WITHOUT_DATA = "NO_DATA";
    private static final Logger logger = LoggerFactory.getLogger(MoneyTransfer.class);

    static void start() {
        final String[] args = {WITHOUT_DATA};
        MoneyTransfer.main(args);
        awaitInitialization();
    }

    static void startWithData() {
        MoneyTransfer.main(null);
        awaitInitialization();
    }

    static void stop() {
        stop();
        awaitStop();
    }

    public static void main(String[] args) {
        port(9999);
        generateData(args);
        threadPool(10);
        after((req, res) -> res.type("application/json"));

        holderRoutes();
        accountRoutes();
        initTransactionRoutes();
        initExceptionsHandling();
        notFoundRoutes();
    }

    private static void holderRoutes() {
        // http://localhost:9999/holders?limit=10
        // http://localhost:9999/holders?page=2&limit=20
        get("/holders", (req, res) -> {
            final Repository<Holder> repository = TransferService.getInstance().getContext().getHolderRepository();
            final PaginationParams pgParams = PaginationParams.from(req);
            return JsonUtils.make().toJson(repository.getAll(pgParams));
        });

        // http://localhost:9999/holders/1
        get("/holders/:id", (req, res) -> {
            final Repository<Holder> repository = TransferService.getInstance().getContext().getHolderRepository();
            return JsonUtils.make().toJson(findById(Holder.class, repository, req));
        });

        // http://localhost:9999/holders/1/accounts
        get("/holders/:id/accounts", (req, res) -> {
            final Repository<Holder> repository = TransferService.getInstance().getContext().getHolderRepository();
            final Holder holder = findById(Holder.class, repository, req);
            final AccountsRepository accountsRepository = TransferService.getInstance().getContext().getAccountsRepository();
            return JsonUtils.make().toJson(accountsRepository.getByHolder(holder));
        });
    }

    private static void accountRoutes() {
        // http://localhost:9999/accounts?limit=10
        get("/accounts", (req, res) -> {
            final Repository<Account> repository = TransferService.getInstance().getContext().getAccountsRepository();
            final PaginationParams pgParams = PaginationParams.from(req);
            return JsonUtils.make().toJson(repository.getAll(pgParams));
        });

        // http://localhost:9999/accounts/1
        get("/accounts/:id", (req, res) -> {
            final Repository<Account> repository = TransferService.getInstance().getContext().getAccountsRepository();
            return JsonUtils.make().toJson(findById(Account.class, repository, req));
        });

        // http://localhost:9999/accounts/1/transactions?limit=100
        get("/accounts/:id/transactions", (req, res) -> {
            final Repository<Account> repository = TransferService.getInstance().getContext().getAccountsRepository();
            final Account account = findById(Account.class, repository, req);
            final TransactionRepository transactionRepository = TransferService.getInstance().getContext().getTransactionRepository();
            final PaginationParams pgParams = PaginationParams.from(req);
            return JsonUtils.make().toJson(transactionRepository.getByAccount(account, pgParams));
        });
    }

    private static void initTransactionRoutes() {
        // http://localhost:9999/transactions?limit=100
        get("/transactions", (req, res) -> {
            final Repository<Transaction> repository = TransferService.getInstance().getContext().getTransactionRepository();
            final PaginationParams pgParams = PaginationParams.from(req);
            return JsonUtils.make().toJson(repository.getAll(pgParams));
        });

        // http://localhost:9999/transactions/1
        get("/transactions/:id", (req, res) -> {
            final Repository<Transaction> repository = TransferService.getInstance().getContext().getTransactionRepository();
            return JsonUtils.make().toJson(findById(Transaction.class, repository, req));
        });

        // http://localhost:9999/transactions
        post("/transactions", (req, res) -> {
            final TransactionPayload payload = JsonUtils.make().fromJson(req.body(), TransactionPayload.class);
            final Transaction trn = TransferService.getInstance().transfer(payload);
            res.status(HttpServletResponse.SC_CREATED);
            res.header("Location", "/transactions/" + trn.getId());
            return JsonUtils.make().toJson(trn);
        });
    }

    public static void notFoundRoutes(){
        get("*", (request, response) -> {
            throw new NotFoundException(String.format("Route {%s} not found!", request.pathInfo()));
        });

        post("*", (request, response) -> {
            throw new NotFoundException(String.format("Route {%s} not found!", request.pathInfo()));
        });

        delete("*", (request, response) -> {
            throw new NotFoundException(String.format("Route {%s} not found!", request.pathInfo()));
        });

        get("*", (request, response) -> {
            throw new NotFoundException(String.format("Route {%s} not found!", request.pathInfo()));
        });
    }

    private static void initExceptionsHandling() {
        exception(IllegalArgumentException.class, (e, req, res) ->
                fillErrorInfo(res, e, HttpServletResponse.SC_BAD_REQUEST));

        exception(NullPointerException.class, (e, req, res) ->
                fillErrorInfo(res, e, HttpServletResponse.SC_BAD_REQUEST));

        exception(NumberFormatException.class, (e, req, res) ->
                fillErrorInfo(res, e, HttpServletResponse.SC_BAD_REQUEST));

        exception(NoSuchElementException.class, (e, req, res) ->
                fillErrorInfo(res, e, HttpServletResponse.SC_NOT_FOUND));

        exception(NotFoundException.class, (e, req, res) ->
                fillErrorInfo(res, e, HttpServletResponse.SC_NOT_FOUND));
    }

    private static void generateData(String[] args) {
        if (args != null && args.length > 0 && WITHOUT_DATA.equalsIgnoreCase(args[0])) {
            return;
        }

        try {
            TransferService.getInstance().generateData();
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    private static void fillErrorInfo(Response res, Exception err, int errCode) {
        res.type("application/json");
        res.status(errCode);
        res.body(JsonUtils.toJson(err, errCode));
    }

    private static <T extends Base> T findById(Class<T> type, Repository<T> repository, Request req) {
        final String id = getId(req);
        final T t = repository.getById(id);
        if (t.isNotValid()) {
            throw new NoSuchElementException(String.format("%s with id %s is not found", type.getSimpleName(), id));
        }
        return t;
    }

    private static String  getId(Request req) {
        return req.params("id");
    }
}
