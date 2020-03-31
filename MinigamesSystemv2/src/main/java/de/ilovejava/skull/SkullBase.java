package de.ilovejava.skull;

import java.util.HashMap;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import de.ilovejava.tdg.Skull;

public class SkullBase {
	HashMap<String, ItemStack> skulls = new HashMap<>();
	private String[] name = {"ILoveJava","Hyzenthay_Rah","KingMontegro","Noronei","_Minemacs_","martoffel_2","VicoKasa","Icetea3125","Serratt","Stacy148","Ch3ckerJan","DieMingos"};
	
	public SkullBase() {
		for(String key : name) {
			ItemStack i = Skull.getSkull(getTexture(key));
			SkullMeta sk = (SkullMeta) i.getItemMeta();
			sk.setDisplayName("§c"+key);
			i.setItemMeta(sk);
			skulls.put(key, i);
		}
	}
	
	private String getTexture(String name) {
		String texture = null;
		switch(name) {
			case "DieMingos":
				texture = "http://textures.minecraft.net/texture/7d98bc2db491a2555a88755cb7fc1827839179e0a90208a9567f709709ad6ae7";
				break;
			case "Ch3ckerJan":
				texture = "http://textures.minecraft.net/texture/bf977186c0982953095d88437c76ab21efd5501ba969e0f95a15aa2d45597d74";
				break;
			case "Stacy148":
				texture = "http://textures.minecraft.net/texture/d1249c5961acf563fec0ff1b519cbb52bf84966117d1a805be3c241a3e20b48f";
				break;
			case "Serratt":
				texture = "http://textures.minecraft.net/texture/35d016f9316013788cb529089a3bbf1dc0ef6baf32463ef620dc5c6a1de82401";
				break;
			case "Icetea3125":
				texture = "http://textures.minecraft.net/texture/ce3b229913af227927e902d07980d4cf471a5249c3a440a762ea215d49c1fca7";
				break;
			case "VicoKasa":
				texture = "http://textures.minecraft.net/texture/fa128015f93f86fb60edf0c5aecc31164ca1c0389d4e6f613d5b7455695815c0";
				break;
			case "martoffel_2":
				texture = "http://textures.minecraft.net/texture/13e6ce82d98368d6144b57dbb3321fa3bd341dd5a9c1b483dee0a0b5d1aef3a3";
				break;
			case "_Minemacs_":
				texture = "http://textures.minecraft.net/texture/7e2f79f9b4c2ceb0b1807c3ecad4b2b68d2ae620fb677ae2098242d041698632";
				break;
			case "Noronei":
				texture = "http://textures.minecraft.net/texture/2a875390513381fdcedc5c731a816ddc6f57fa5e9c287b488bb19d08f1f7ec39";
				break;
			case "KingMontegro":
				texture = "http://textures.minecraft.net/texture/a6df8b0d0440045d9977859614bd9d1bba7218ce3f098d55ae4b3f6258c7ff14";
				break;
			case "Hyzenthay_Rah":
				texture = "http://textures.minecraft.net/texture/bd65cc7db290bd320911d2ae9d00d1eb011ef6993fb476c8d33706e3c5f5a63";
				break;
			case "ILoveJava":
				texture = "http://textures.minecraft.net/texture/82910ad8b7c5c563f5a251ca08a174c14ef9e529b05cf79dc792ef4ec01814a5";
				break;
		}
		return texture;
	}
	
	public ItemStack getSkull(String key) {
		return skulls.get(key);
	}
}
