package de.ilovejava.vault;

import de.ilovejava.user.User;
import de.ilovejava.utils.Utils;
import de.ilovejava.uuid.uuidfetcher;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.OfflinePlayer;

import java.util.List;

public class VaultConnector implements Economy{

	@Override
	public EconomyResponse bankBalance(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse bankDeposit(String arg0, double arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse bankHas(String arg0, double arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse bankWithdraw(String arg0, double arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse createBank(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse createBank(String arg0, OfflinePlayer arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean createPlayerAccount(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createPlayerAccount(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createPlayerAccount(OfflinePlayer arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String currencyNamePlural() {
		return "Arden";
	}

	@Override
	public String currencyNameSingular() {
		return "Arden";
	}

	@Override
	public EconomyResponse deleteBank(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse depositPlayer(String name, double amnt) {
		String uuid = uuidfetcher.getUUID(name).toString();
		User u = Utils.getUser().get(uuid);
		return new EconomyResponse(amnt, u.addArden(amnt),ResponseType.SUCCESS, "");
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer arg0, double arg1) {
		return depositPlayer(arg0.getName(), arg1);
	}

	@Override
	public EconomyResponse depositPlayer(String arg0, String arg1, double arg2) {
		return depositPlayer(arg0, arg2);
	}

	@Override
	public EconomyResponse depositPlayer(OfflinePlayer arg0, String arg1, double arg2) {
		return depositPlayer(arg0.getName(), arg2);
	}

	@Override
	public String format(double arg0) {
		return "A";
	}

	@Override
	public int fractionalDigits() {
		return 2;
	}

	@Override
	public double getBalance(String name) {
		String uuid = uuidfetcher.getUUID(name).toString();
		return Utils.getUser().get(uuid).Arden();
	}

	@Override
	public double getBalance(OfflinePlayer arg0) {
		return getBalance(arg0.getName());
	}

	@Override
	public double getBalance(String arg0, String arg1) {
		return getBalance(arg0);
	}

	@Override
	public double getBalance(OfflinePlayer arg0, String arg1) {
		return getBalance(arg0.getName());
	}

	@Override
	public List<String> getBanks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return "Arden";
	}

	@Override
	public boolean has(String name, double amnt) {
		String uuid = uuidfetcher.getUUID(name).toString();
		
		return Utils.getUser().get(uuid).Arden() >= amnt;
	}

	@Override
	public boolean has(OfflinePlayer arg0, double arg1) {
		return has(arg0.getName(), arg1);
	}

	@Override
	public boolean has(String arg0, String arg1, double arg2) {
		return has(arg0, arg2);
	}

	@Override
	public boolean has(OfflinePlayer arg0, String arg1, double arg2) {
		return has(arg0.getName(), arg2);
	}

	@Override
	public boolean hasAccount(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasAccount(OfflinePlayer arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasAccount(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasAccount(OfflinePlayer arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasBankSupport() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public EconomyResponse isBankMember(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse isBankMember(String arg0, OfflinePlayer arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse isBankOwner(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EconomyResponse isBankOwner(String arg0, OfflinePlayer arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEnabled() {
		return Utils.getInstance().isEnabled();
	}

	@Override
	public EconomyResponse withdrawPlayer(String name, double amnt) {
		String uuid = uuidfetcher.getUUID(name).toString();
		User u = Utils.getUser().get(uuid);
		u.removeArden(u.toRemoveArden()+amnt);
		return new EconomyResponse(amnt, u.Arden(), u.removeArden(amnt) ? ResponseType.SUCCESS : ResponseType.FAILURE, "Fehler gefunden!");
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer arg0, double arg1) {
		return withdrawPlayer(arg0.getName(), arg1);
	}

	@Override
	public EconomyResponse withdrawPlayer(String arg0, String arg1, double arg2) {
		return withdrawPlayer(arg0, arg2);
	}

	@Override
	public EconomyResponse withdrawPlayer(OfflinePlayer arg0, String arg1, double arg2) {
		return withdrawPlayer(arg0.getName(), arg2);
	}

}
