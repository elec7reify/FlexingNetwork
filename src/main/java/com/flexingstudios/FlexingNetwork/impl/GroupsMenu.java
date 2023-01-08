package com.flexingstudios.FlexingNetwork.impl;

import com.flexingstudios.FlexingNetwork.api.Language.Messages;
import com.flexingstudios.FlexingNetwork.api.menu.InvMenu;
import com.flexingstudios.FlexingNetwork.api.player.Language;
import com.flexingstudios.FlexingNetwork.api.util.ItemBuilder;
import com.flexingstudios.FlexingNetwork.api.util.Items;
import com.flexingstudios.FlexingNetwork.api.util.SkullBuilder;
import com.flexingstudios.FlexingNetwork.impl.player.FlexCoinsMenu;
import com.google.common.collect.ImmutableSet;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GroupsMenu implements InvMenu {
    private static final Set<Integer> GLASS_PANE_SLOTS = ImmutableSet.of(0, 1, 2, 3, 4, 5, 6, 7, 9, 13, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 31, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53);
    public final Inventory inv;

    public GroupsMenu(Player player) {
        inv = Bukkit.createInventory(this, 54, "Донат привилегии | Выживание");
        List<String> lore = new ArrayList<>();

        ItemStack GLASS_PANE = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemMeta GLASS_PANE_META = GLASS_PANE.getItemMeta();
        GLASS_PANE_META.setDisplayName("§6§k|§a§k|§e§k|§c§k|");
        GLASS_PANE.setItemMeta(GLASS_PANE_META);
        GLASS_PANE_SLOTS.forEach(slot -> inv.setItem(slot, GLASS_PANE));

        inv.setItem(8, Items.name(new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/647cf0f3b9ec9df2485a9cd4795b60a391c8e6ebac96354de06e3357a9a88607", 1)).build(), Language.getMsg(player, Messages.CLOSE_DONATE_INVENTORY)));
        inv.setItem(10, Items.name(Items.glow(Material.GRASS), "&f&lВы просматриваете возможности на &a&lВыживании"));
        inv.setItem(11, Items.name(Material.TNT, "&f&lВозможности на &c&lАнархии"));
        inv.setItem(12, Items.name(Material.IRON_SWORD, "&f&lВозможности на &b&lМини-играх"));
        inv.setItem(14, Items.name(Material.QUARTZ, "&e&lКуда уйдут деньги с покупки?", "&7Прочитайте этот пункт", "&7Если вам интересно", "&7Что произойдёт с вашими деньгами"));
        this.inv.setItem(15, Items.name(Items.glow(Material.REDSTONE), "&c&lОбратите внимание", "&7Прочитайте этот пункт", "&7И узнайте об особенностях покупки", "&7Которые нужно учитывать"));
        inv.setItem(16, Items.name(Material.MAGMA_CREAM, "&a&lВозможности Админов", "&7Рекомендуется прочитать этот пункт", "&7Чтобы узнать больше об Админах", "&7И их возможностях"));
        inv.setItem(40, Items.name(Items.glow(Material.ENDER_PEARL), "&9&lFlex&f&lCoins"));

        // Статус VIP (Чикибамбвипка)
        ItemStack vip = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/2b6a97ba2793fe1fc83fd261e6de8ac3299f9f646f322bb8d40554baaecff", 1)).build();
        for (String s : Language.getList(player, Messages.RANK_VIP_LORE_SURVIVAL)) {
            lore.add(s.replace("{player_name}", player.getName()));
        }
        inv.setItem(28, Items.name(vip, "&fПривилегия &a&lЧикибамбвипка &f- &a&l29 Руб.&f/мес.", lore));
        lore.clear();

        // Статус premium (Премиумбамбони)
        ItemStack premium = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/279a2375363be891381d42c45b377553c2b6e0fbdc16def5fbc6b32ed5cad7a7", 1)).build();
        this.inv.setItem(29, Items.name(premium, "&fПривилегия &5&lПремиумбамбони &f- &a&l49 Руб.&f/мес.",
                "",
                " &5• &fПрефикс в чат и таб: &7«&5&lПремиумбамбони&7» &7" + player.getDisplayName(),
                " &5• &fПример сообщения в чате:",
                "&7«&5&lПремиумбамбони&7» &7" + player.getDisplayName() + " &8→ &7Hello World!",
                "",
                " &5◆ &fВозможность получить набор &5&lPREMIUM: &5/kit premium",
                " &5◆ &fВозможность одеть блок или предмет на голову: &5/hat",
                " &5◆ &fВозможность установить &5&l6 &fточек дома: &5/sethome",
                " &5◆ &fВозможность восстановить голод: &5/feed",
                " &5◆ &fВозможность очистить себе инвентарь: &5/ci",
                " &5◆ &fВозможность включить режим бессмертия: &5/god",
                " &5◆ &fВозможность отключить телепорт к себе: &5/tptoggle",
                " &5◆ &fВозможность показать ближайших игроков: &5/near",
                "",
                " &c▪ Донат выдается на &c&lвсе &cсервера.",
                " &6▪ Возможности &6&lпредыдущих &6донатов.",
                " &a▪ Донат &a&lостается &aпосле вайпа.",
                " &f▪ Вы получите &b&l&n100&9&l Flex&f&lCoin &fза покупку",
                "",
                "&fПокупать донат на сайте: &3www.FlexingWorld.net"));

        // Статус creative (Чикикреатив)
        ItemStack creative = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/f718dbf107c6a31763b30298d1b09fe9e836da634c556723ee41a475ddb2", 1)).build();
        this.inv.setItem(30, Items.name(creative, "&fПривилегия &9&lЧикикреатив &f- &a&l69 Руб.&f/мес.",
                "",
                " &9• &fПрефикс в чат и таб: &7«&9&lЧикикреатив&7» &7" + player.getDisplayName(),
                " &9• &fПример сообщения в чате:",
                "&7«&9&lЧикикреатив&7» &7" + player.getDisplayName() + " &8→ &7Hello World!",
                "",
                " &9◆ &fВозможность запросить телепорт к себе: &9/tpahere",
                " &9◆ &fВозможность установить &9&l12 &fточек дома: &9/sethome",
                " &9◆ &fВозможность установить творческий режим: &9/gamemode 1",
                " &9◆ &fВозможность телепорт по координатам: &9/tppos",
                " &9◆ &fВозможность узнать координаты: &9/getpos",
                " &9◆ &fВозможность узнать настоящее имя игрока: &9/realname",
                " &9◆ &fВозможность изменить для себя время: &9/ptime",
                " &9◆ &fВозможность изменить для себя погоду: &9/pweather",
                "",
                " &c▪ Донат выдается на &c&lвсе &cсервера.",
                " &6▪ Возможности &6&lпредыдущих &6донатов.",
                " &a▪ Донат &a&lостается &aпосле вайпа.",
                " &f▪ Вы получите &b&l&n130&9&l Flex&f&lCoin &fза покупку",
                "",
                "&fПокупать донат на сайте: &3www.FlexingWorld.net"));

        // Статус moder (Чикимодератор)
        ItemStack moder = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/78d26c4293faad7e0fd1e91c71dc58b4ebc38c90dabac915559768d2d9f4af", 1)).build();
        this.inv.setItem(37, Items.name(moder, "&fПривилегия &e&lЧикимодератор &f- &a&l99 Руб.",
                "",
                " &e• &fПрефикс в чат и таб: &7«&e&lЧикимодератор&7» &7" + player.getDisplayName(),
                " &e• &fПример сообщения в чате:",
                "&7«&e&lЧикимодератор&7» &7" + player.getDisplayName() + " &8→ &7Hello World!",
                "",
                " &e◆ &fВозможность выдать себе опыт: &e/exp",
                " &e◆ &fВозможность установить &e&l15 &fточек дома: &e/sethome",
                " &e◆ &fВозможность восстановить здоровье: &e/heal",
                " &e◆ &fВозможность перейти в AFK: &e/afk",
                " &e◆ &fВозможность редактировать книги: &e/book",
                " &e◆ &fВозможность проверить баланс игрока: &e/bal &7[ник]",
                " &e◆ &fВозможность потушить себя: &e/ext",
                "",
                " &c▪ Донат выдается на &c&lвсе &cсервера.",
                " &6▪ Возможности &6&lпредыдущих &6донатов.",
                " &a▪ Донат &a&lостается &aпосле вайпа.",
                " &f▪ Вы получите &b&l&n200&9&l Flex&f&lCoin &fза покупку",
                "",
                "&fПокупать донат на сайте: &3www.FlexingWorld.net"));

        // Статус chikibamboni (Чикибамбони)
        ItemStack chikibamboni = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/3f3c11c62e72fc963ea5bbda649ea9d45497256736fe3c1df1f2e38cf935b72", 1)).build();
        this.inv.setItem(38, Items.name(chikibamboni, "&fПривилегия &3&lЧикибамбони &f- &a&l129 Руб.",
                "",
                " &3• &fПрефикс в чат и таб: &7«&3&lЧикибамбони&7» &7" + player.getDisplayName(),
                " &3• &fПример сообщения в чате:",
                "&7«&3&lЧикибамбони&7» &7" + player.getDisplayName() + " &8→ &7Hello World!",
                "",
                " &3◆ &fВозможность тп к игрокам: &3/tp",
                " &3◆ &fВозможность установить &3&l20&f точек дома: &3/sethome",
                " &3◆ &fВозможность установить режим наблюдателя: &3/gamemode 3",
                " &3◆ &fВозможность установить режим приключений: &3/gamemode 2",
                " &3◆ &fВозможность переключить режим игры игрокам: &3/gamemode 0-3 &7[ник]",
                " &3◆ &fВозможность телепорт наверх: &3/top",
                "",
                " &c▪ Донат выдается на &c&lвсе &cсервера.",
                " &6▪ Возможности &6&lпредыдущих &6донатов.",
                " &a▪ Донат &a&lостается &aпосле вайпа.",
                " &f▪ Вы получите &b&l&n250&9&l Flex&f&lCoin &fза покупку",
                "",
                "&fПокупать донат на сайте: &3www.FlexingWorld.net"));

        // Статус admin (Чикибамбони)
        ItemStack admin = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/3e427adff356f0945ebba391ca0eef465ae17461ea7c53ad173fbb6c7f36a8", 1)).build();
        this.inv.setItem(39, Items.name(admin, "&fПривилегия &b&lАдминобамбони &f- &a&l179 Руб.",
                "",
                " &b• &fПрефикс в чат и таб: &7«&b&lАдминобамбони&7» &7" + player.getDisplayName(),
                " &b• &fПример сообщения в чате:",
                "&7«&b&lАдминобамбони&7» &7" + player.getDisplayName() + " &8→ &7Hello World!",
                "",
                " &b◆ &fВозможность изменить ник: &b/nick [новый ник]",
                " &b◆ &fВозможность установить время только у себя: &b/ptime",
                " &b◆ &fВозможность установить погоду только у себя: &b/pweather",
                " &b◆ &fВозможность телепортировать игрока к себе: &b/tphere",
                " &b◆ &fБазовый набор WorldEdit: &b//set, //walls, //faces",
                " &b◆ &fВозможность установить &b&l30 &fточек дома: &b/sethome",
                "",
                " &c▪ Донат выдается на &c&lвсе &cсервера.",
                " &6▪ Возможности &6&lпредыдущих &6донатов.",
                " &a▪ Донат &a&lостается &aпосле вайпа.",
                " &f▪ Вы получите &b&l&n360&9&l Flex&f&lCoin &fза покупку",
                "",
                "&fПокупать донат на сайте: &3www.FlexingWorld.net"));

        // Статус chikybambonylla (Админобамбони)
        ItemStack chikybambonylla = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/58b3ffd37e6f32a4e7c1aba2cb484923cf695f447c879e712ec25a613e010", 1)).build();
        this.inv.setItem(32, Items.name(chikybambonylla, "&fПривилегия &2&lЧикибамбонила &f- &a&l219 Руб.",
                "",
                " &2• &fПрефикс в чат и таб: &7«&2&lЧикибамбонила&7» &7" + player.getDisplayName(),
                " &2• &fПример сообщения в чате:",
                "&7«&2&lЧикибамбонила&7» &7" + player.getDisplayName() + " &8→ &7Hello World!",
                "",
                " &2◆ &fВозможность кикнуть недоброжелателя: &2/kick [ник игрока]",
                " &2◆ &fВозможность открыть инвентарь игрока: &2/invsee [ник игрока]",
                " &2◆ &fВозможность увеличить скорость своего перемещения: &2/walkspeed",
                " &2◆ &fВозможность изменить время на сервере: &2/time",
                " &2◆ &fВозможность изменить погоду на сервере: &2/weather",
                " &2◆ &fВозможность самовыпилиться: &2/suicide",
                " &4&l(не повторять в жизни)",
                " &2◆ &fВозможность установить &2&l50 &fточек дома (максимум): &2/sethome",
                " &2◆ &fВозможность писать цветным текстом в чате",
                " &2• &fПример сообщения в чате:",
                "&7«&2&lЧикибамбонила&7» &7" + player.getDisplayName() + " &8→ &3Fisting &5ass",
                "",
                " &c▪ Донат выдается на &c&lвсе &cсервера.",
                " &6▪ Возможности &6&lпредыдущих &6донатов.",
                " &a▪ Донат &a&lостается &aпосле вайпа.",
                " &f▪ Вы получите &b&l&n450&9&l Flex&f&lCoin &fза покупку",
                "",
                "&fПокупать донат на сайте: &3www.FlexingWorld.net"));

        // Статус sponsor (Спонсорбамбони)
        ItemStack sponsor = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/c1df70dc9885e418b58d8e7486a81ed4ec2e7963c42569c468084d4f567c6", 1)).build();
        inv.setItem(33, Items.name(sponsor, "&fПривилегия &d&lСпонсорбамбони &f- &a&l299 Руб.",
                "",
                " &d• &fПрефикс в чат и таб: &7«&d&lСпонсорбамбони&7» &7" + player.getDisplayName(),
                " &d• &fПример сообщения в чате:",
                "&7«&d&lСпонсорбамбони&7» &7" + player.getDisplayName() + " &8→ &7Hello World!",
                "",
                " &d◆ &fВозможность забанить самых настырных: &d/ban 30m",
                " &d◆ &fВозможность выдать флай другу: &d/fly",
                " &d◆ &fВозможность сделать объявление &d/broadcast",
                " &d◆ &fВозможность очистить чат &d/clearchat",
                " &d◆ &fВозможность тихо открыть сундук: &d/silentchest",
                " &d◆ &fВозможность хуйнуть молнией: &d/thor",
                " &d◆ &fВозможность изменить скорость полёта: &d/flyspeed",
                " &d◆ &fВозможность сетать &d650 000 &fблоков",
                "",
                " &c▪ Донат выдается на &c&lвсе &cсервера.",
                " &6▪ Возможности &6&lпредыдущих &6донатов.",
                " &a▪ Донат &a&lостается &aпосле вайпа.",
                " &f▪ Вы получите &b&l&n600&9&l Flex&f&lCoin &fза покупку",
                "",
                "&fПокупать донат на сайте: &3www.FlexingWorld.net"));

        // Статус owner (Чикивладелец)
        ItemStack owner = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/6c92e3f45b49e405670224892f93ebc84fa7f8c96c36aab24a8854f2cbf0b8", 1)).build();
        inv.setItem(34, Items.name(owner, "&fПривилегия &6&lЧикивладелец &f- &a&l449 Руб.",
                "",
                " &6• &fПрефикс в чат и таб: &7«&6&lЧикивладелец&7» &7" + player.getDisplayName(),
                " &6• &fПример сообщения в чате:",
                "&7«&6&lЧикивладелец&7» &7" + player.getDisplayName() + " &8→ &7Hello World!",
                "",
                " &6◆ &fУвеличено допустимое время для бана игрока: &6/ban 2h",
                " &6◆ &fВозможность сетать территорию &6800 000 &fблоков",
                " &6◆ &fВозможность узнать информацию об игроке: &6/whois",
                " &6◆ &fРасширенные возможности WorldEdit:",
                "  &6• //copy",
                "  &6• //paste",
                "  &6• //rotate",
                "  &6• //flip",
                "  &6• //move",
                "  &6• //replace",
                "",
                " &c▪ Донат выдается на &c&lвсе &cсервера.",
                " &6▪ Возможности &6&lпредыдущих &6донатов.",
                " &a▪ Донат &a&lостается &aпосле вайпа.",
                " &f▪ Вы получите &b&l&n900&9&l Flex&f&lCoin &fза покупку",
                "",
                " &c&lПримечание: в данной привилегии присутствуют права,",
                " &c&lЗа неправильное использование которых можно получить",
                " &c&lнаказание. Вы несёте ответственность за свои действия.",
                "",
                "&fПокупать донат на сайте: &3www.FlexingWorld.net"));

        // Статус bombaster (Чикибомбастер)
        ItemStack bombaster = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/b88fb6b2b3efa6ab299f9f4e7d8c1a1d7338753bdf8fef81075f2155943bc69", 1)).build();
        this.inv.setItem(41, Items.name(bombaster, "&fПривилегия &c&lЧикибомбастер &f- &a&l699 Руб.",
                "",
                " &c• &fПрефикс в чат и таб: &7«&c&lЧикибомбастер&7» &f" + player.getDisplayName(),
                " &c• &fПример сообщения в чате:",
                "&7«&c&lЧикибомбастер&7» &f" + player.getDisplayName() + " &8→ &7Hello World!",
                "",
                " &c◆ &fПолный доступ к &c&n&lRTP:&c /rtp help",
                " &c◆ &fДоступ к ванишу: &c/vanish",
                " &c◆ &fУвеличено допустимое время для бана игрока: &c/ban 1week",
                " &c◆ &fРасширенные возможности WorldEdit:",
                "  &c• //cyl",
                "  &c• //hcyl",
                "  &c• //pyramid",
                "  &c• //hpyramid",
                "  &c• /brush sphere/cyl/smooth",
                "  &c• /mask",
                "",
                " &c▪ Донат выдается на &c&lвсе &cсервера.",
                " &6▪ Возможности &6&lпредыдущих &6донатов.",
                " &a▪ Донат &a&lостается &aпосле вайпа.",
                " &f▪ Вы получите &b&l&n1500&9&l Flex&f&lCoin &fза покупку",
                "",
                " &c&lПримечание: в данной привилегии присутствуют права,",
                " &c&lЗа неправильное использование которых можно получить",
                " &c&lнаказание. Вы несёте ответственность за свои действия.",
                "",
                "&fПокупать донат на сайте: &3www.FlexingWorld.net"));

        // Статус god (Чикибомбастер)
        ItemStack god = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/b88fb6b2b3efa6ab299f9f4e7d8c1a1d7338753bdf8fef81075f2155943bc69", 1)).build();
        this.inv.setItem(42, Items.name(god, "&fПривилегия &4&lЧикибамбог &f- &a&l2499 Руб.",
                "",
                " &4• &fПрефикс в чат и таб: &7«&4&lЧикибамбог&7» &f" + player.getDisplayName(),
                " &4• &fПример сообщения в чате:",
                "&7«&4&lЧикибамбог&7» &f" + player.getDisplayName() + " &8→ &7Hello World!",
                " &4• &fВключён &4&n&lиммунитет&f от бана/кика &7(но Админ сервера сможет забанить)",
                "",
                "&c&lВоистину королевская привилегия для олигархичей",
                "&c&lЛучшее решение на сервере, которое Вы можете себе позволить",
                "",
                " &4◆ &c60% команд сервера",
                " &4◆ &fПолный доступ к &6CMI",
                " &4◆ &fВозможность заприватить &7неограниченное &fколичество регионов по &71 000 000 &fблоков",
                " &4◆ &fС вас снято большинство ограничений и лимитов на &a&n&lкоманды",
                " &4◆ &4&n&lЭКСКЛЮЗИВ:&f доступ к Бану по IP на 30 минут",
                " &4◆ &4&n&lЭКСКЛЮЗИВ:&f доступ к Муту на 10 часов",
                "",
                " &4◆ &fДоступ к вершине этого сервера",
                " &4◆ &fВыше тебя будет только Администрация",
                " &4◆ &c&lЛучший донат для тех, кто хочет вписать своё имя в историю сервера",
                " &4◆ &4&lСтань ебучей легендой Найт-сити!",
                "",
                " &c▪ Донат выдается на &c&lвсе &cсервера.",
                " &6▪ Возможности &6&lпредыдущих &6донатов.",
                " &a▪ Донат &a&lостается &aпосле вайпа.",
                " &f▪ Вы получите &b&l&n5000&9&l Flex&f&lCoin &fза покупку",
                "",
                " &c&lПримечание: в данной привилегии присутствуют права,",
                " &c&lЗа неправильное использование которых можно получить",
                " &c&lнаказание. Вы несёте ответственность за свои действия.",
                "",
                "&fПокупать донат на сайте: &3www.FlexingWorld.net"));

        // Информация
        ItemStack information = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/9c96be7886eb7df75525a363e5f549626c21388f0fda988a6e8bf487a53", 1)).build();

        this.inv.setItem(43, Items.name(information, "&2&lИнформация", "", "&fЧтобы приобрести привилегию, нужно выполнить простой алгоритм:",
                "&a• &fПерейти на сайт для покупки доната: &c&lFlexingWorld.net", "&a• &fВвести в первое поле свой игровой никнейм",
                "&a• &fВыбрать привилегию во втором поле", "&a• &fПерейти по кнопке &aкупить &fна форму оплаты", "",
                "&4&l[!] &fДонат привилегия будет выдана", "    &fмоментально и автоматически после покупки доната!", "",
                "&eСайт покупки доната", "&ehttps:// &fFlexingWorld.net"));
    }

    @Override
    public void onClick(ItemStack is, Player player, int slot, ClickType clicktype) {
        switch (slot) {
            case 8:
                player.closeInventory();
                break;
            case 11:
                player.openInventory(new AnarchyMenu(player).getInventory());
                break;
            case 12:
                player.openInventory(new MiniGamesGroups(player).getInventory());
                break;
            case 14:
                player.openInventory(new MoneyMenu(player).getInventory());
                break;
            case 15:
                player.openInventory(new CautionMenu(player).getInventory());
                break;
            case 16:
                player.openInventory(new AdminsMenu(player).getInventory());
                break;
            case 40:
                player.openInventory(new FlexCoinsMenu(player).getInventory());
                break;
        }
    }

    @Override
    public Inventory getInventory() {
        return this.inv;
    }

    private static class MiniGamesGroups implements InvMenu {
        private static final Set<Integer> GLASS_PANE_SLOT = ImmutableSet.of(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(9), Integer.valueOf(13), Integer.valueOf(17), Integer.valueOf(18), Integer.valueOf(19), Integer.valueOf(20), Integer.valueOf(21), Integer.valueOf(22), Integer.valueOf(23), Integer.valueOf(24), Integer.valueOf(25), Integer.valueOf(26), Integer.valueOf(27), Integer.valueOf(31), Integer.valueOf(35), Integer.valueOf(36), Integer.valueOf(44), Integer.valueOf(45), Integer.valueOf(46), Integer.valueOf(47), Integer.valueOf(48), Integer.valueOf(49), Integer.valueOf(50), Integer.valueOf(51), Integer.valueOf(52), Integer.valueOf(53));
        private final Inventory inv;

        public MiniGamesGroups(Player player) {
            this.inv = Bukkit.createInventory(this, 54, "Донат привилегии | Мини-игры");

            ItemStack GLASS_PANE = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
            ItemMeta GLASS_PANE_META = GLASS_PANE.getItemMeta();
            GLASS_PANE_META.setDisplayName("§6§k|§a§k|§e§k|§c§k|");
            GLASS_PANE.setItemMeta(GLASS_PANE_META);
            this.GLASS_PANE_SLOT.forEach(slot -> this.inv.setItem(slot.intValue(), GLASS_PANE));

            this.inv.setItem(8, Items.name(new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/647cf0f3b9ec9df2485a9cd4795b60a391c8e6ebac96354de06e3357a9a88607", 1)).build(), "&3&lНа главную"));
            this.inv.setItem(10, Items.name(Material.GRASS, "&f&lВозможности на &a&lВыживании"));
            this.inv.setItem(11, Items.name(Material.TNT, "&f&lВозможности на &c&lАнархии"));
            this.inv.setItem(12, Items.name(Items.glow(Material.IRON_SWORD), "&f&lВы просматриваете возможности на &b&lМини-играх"));
            this.inv.setItem(14, Items.name(Material.QUARTZ, "&e&lКуда уйдут деньги с покупки?", "&7Прочитайте этот пункт", "&7Если вам интересно", "&7Что произойдёт с вашими деньгами"));
            this.inv.setItem(15, Items.name(Items.glow(Material.REDSTONE), "&c&lОбратите внимание", "&7Прочитайте этот пункт", "&7И узнайте об особенностях покупки", "&7Которые нужно учитывать"));
            this.inv.setItem(16, Items.name(Material.MAGMA_CREAM, "&a&lВозможности Админов", "&7Рекомендуется прочитать этот пункт", "&7Чтобы узнать больше об Админах", "&7И их возможностях"));
            this.inv.setItem(40, Items.name(Items.glow(Material.ENDER_PEARL), "&9&lFlex&f&lCoins"));

            // Статус VIP (Чикибамбвипка)
            ItemStack vip = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/595aca72dcce1298475398fb7adff8447c9a670e6cf64dd3933bbc94918e99", 1)).build();

            this.inv.setItem(28, Items.name(vip, "&fПривилегия &a&lЧикибамбвипка &f- &a&l29 Руб.&f/мес.",
                    "",
                    " &a• &fПрефикс в чат и таб: &7«&a&lЧикибамбвипка&7» &7" + player.getDisplayName(),
                    " &a• &fПример сообщения в чате:",
                    "&7«&a&lЧикибамбвипка&7» &7" + player.getDisplayName() + " &8→ &7Hello World!",
                    "",
                    "&c&lУ данной привилегии прав на мини-играх нет!",
                    "",
                    " &c▪ Донат выдается на &c&lвсе &cсервера.",
                    " &a▪ Донат &a&lостается &aпосле вайпа.",
                    " &f▪ Вы получите &b&l&n60&9&l Flex&f&lCoin &fза покупку",
                    "",
                    "&fПокупать донат на сайте: &3www.FlexingWorld.net"));

            // Статус premium (Премиумбамбони)
            ItemStack premium = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/595aca72dcce1298475398fb7adff8447c9a670e6cf64dd3933bbc94918e99", 1)).build();
            this.inv.setItem(29, Items.name(premium, "&fПривилегия &5&lПремиумбамбони &f- &a&l49 Руб.&f/мес.",
                    "",
                    " &5• &fПрефикс в чат и таб: &7«&5&lПремиумбамбони&7» &7" + player.getDisplayName(),
                    " &5• &fПример сообщения в чате:",
                    "&7«&5&lПремиумбамбони&7» &7" + player.getDisplayName() + " &8→ &7Hello World!",
                    "",
                    "&c&lУ данной привилегии прав на мини-играх нет!",
                    "",
                    " &c▪ Донат выдается на &c&lвсе &cсервера.",
                    " &6▪ Возможности &6&lпредыдущих &6донатов.",
                    " &a▪ Донат &a&lостается &aпосле вайпа.",
                    " &f▪ Вы получите &b&l&n100&9&l Flex&f&lCoin &fза покупку",
                    "",
                    "&fПокупать донат на сайте: &3www.FlexingWorld.net"));

            // Статус creative (Чикикреатив)
            ItemStack creative = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/595aca72dcce1298475398fb7adff8447c9a670e6cf64dd3933bbc94918e99", 1)).build();
            this.inv.setItem(30, Items.name(creative, "&fПривилегия &9&lЧикикреатив &f- &a&l69 Руб.&f/мес.",
                    "",
                    " &9• &fПрефикс в чат и таб: &7«&9&lЧикикреатив&7» &7" + player.getDisplayName(),
                    " &9• &fПример сообщения в чате:",
                    "&7«&9&lЧикикреатив&7» &7" + player.getDisplayName() + " &8→ &7Hello World!",
                    "",
                    "&c&lУ данной привилегии прав на мини-играх нет!",
                    "",
                    " &c▪ Донат выдается на &c&lвсе &cсервера.",
                    " &6▪ Возможности &6&lпредыдущих &6донатов.",
                    " &a▪ Донат &a&lостается &aпосле вайпа.",
                    " &f▪ Вы получите &b&l&n130&9&l Flex&f&lCoin &fза покупку",
                    "",
                    "&fПокупать донат на сайте: &3www.FlexingWorld.net"));

            // Статус moder (Чикимодератор)
            ItemStack moder = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/595aca72dcce1298475398fb7adff8447c9a670e6cf64dd3933bbc94918e99", 1)).build();
            this.inv.setItem(37, Items.name(moder, "&fПривилегия &e&lЧикимодератор &f- &a&l99 Руб.",
                    "",
                    " &e• &fПрефикс в чат и таб: &7«&e&lЧикимодератор&7»  &7" + player.getDisplayName(),
                    " &e• &fПример сообщения в чате:",
                    "&7«&e&lЧикимодератор&7» &7" + player.getDisplayName() + " &8→ &7Hello World!",
                    "",
                    "&c&lУ данной привилегии прав на мини-играх нет!",
                    "",
                    " &c▪ Донат выдается на &c&lвсе &cсервера.",
                    " &6▪ Возможности &6&lпредыдущих &6донатов.",
                    " &a▪ Донат &a&lостается &aпосле вайпа.",
                    " &f▪ Вы получите &b&l&n200&9&l Flex&f&lCoin &fза покупку",
                    "",
                    "&fПокупать донат на сайте: &3www.FlexingWorld.net"));

            // Статус chikibamboni (Чикибамбони)
            ItemStack chikibamboni = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/595aca72dcce1298475398fb7adff8447c9a670e6cf64dd3933bbc94918e99", 1)).build();
            this.inv.setItem(38, Items.name(chikibamboni, "&fПривилегия &3&lЧикибамбони &f- &a&l129 Руб.",
                    "",
                    " &3• &fПрефикс в чат и таб: &7«&3&lЧикибамбони&7» &7" + player.getDisplayName(),
                    " &3• &fПример сообщения в чате:",
                    "&7«&3&lЧикибамбони&7» &7" + player.getDisplayName() + " &8→ &7Hello World!",
                    "",
                    "&c&lУ данной привилегии прав на мини-играх нет!",
                    "",
                    " &c▪ Донат выдается на &c&lвсе &cсервера.",
                    " &6▪ Возможности &6&lпредыдущих &6донатов.",
                    " &a▪ Донат &a&lостается &aпосле вайпа.",
                    " &f▪ Вы получите &b&l&n250&9&l Flex&f&lCoin &fза покупку",
                    "",
                    "&fПокупать донат на сайте: &3www.FlexingWorld.net"));

            // Статус admin (Админобамбони)
            ItemStack admin = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/3e427adff356f0945ebba391ca0eef465ae17461ea7c53ad173fbb6c7f36a8", 1)).build();
            this.inv.setItem(39, Items.name(admin, "&fПривилегия &b&lАдминобамбони &f- &a&l179 Руб.",
                    "",
                    " &b• &fПрефикс в чат и таб: &7«&b&lАдминобамбони&7» &7" + player.getDisplayName(),
                    " &b• &fПример сообщения в чате:",
                    "&7«&b&lАдминобамбони&7» &7" + player.getDisplayName() + " &8→ &7Hello World!",
                    "",
                    " &b◆ &fВозможность изменить ник: &b/nick [новый ник]",
                    " &b◆ &fВозможность узнать настоящий ник игрока: &b/realname [ник игрока]", "",
                    " &c▪ Донат выдается на &c&lвсе &cсервера.",
                    "",
                    " &6▪ Возможности &6&lпредыдущих &6донатов.",
                    " &a▪ Донат &a&lостается &aпосле вайпа.",
                    " &f▪ Вы получите &b&l&n360&9&l Flex&f&lCoin &fза покупку",
                    "",
                    "&fПокупать донат на сайте: &3www.FlexingWorld.net"));

            // Статус chikybambonylla (Чикибамбонила)
            ItemStack chikybambonylla = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/58b3ffd37e6f32a4e7c1aba2cb484923cf695f447c879e712ec25a613e010", 1)).build();
            this.inv.setItem(32, Items.name(chikybambonylla, "&fПривилегия &2&lЧикибамбонила &f- &a&l219 Руб.",
                    "",
                    " &2• &fПрефикс в чат и таб: &7«&2&lЧикибамбонила&7» &7" + player.getDisplayName(),
                    " &2• &fПример сообщения в чате:",
                    "&7«&2&lЧикибамбонила&7» &7" + player.getDisplayName() + " &8→ &7Hello World!",
                    "",
                    " &2◆ &fВозможность кикнуть недоброжелателя: &2/kick [ник игрока]",
                    " &2◆ &fВозможность писать цветным текстом в чате",
                    " &2• &fПример сообщения в чате:",
                    "&7«&2&lЧикибамбонила&7» &7" + player.getDisplayName() + " &8→ &3Fisting &5ass",
                    "",
                    " &c▪ Донат выдается на &c&lвсе &cсервера.",
                    " &6▪ Возможности &6&lпредыдущих &6донатов.",
                    " &a▪ Донат &a&lостается &aпосле вайпа.",
                    " &f▪ Вы получите &b&l&n450&9&l Flex&f&lCoin &fза покупку",
                    "",
                    " &c&lПримечание: в данной привилегии присутствуют права,",
                    " &c&lЗа неправильное использование которых можно получить",
                    " &c&lнаказание. Вы несёте ответственность за свои действия.",
                    "",
                    "&fПокупать донат на сайте: &3www.FlexingWorld.net"));

            // Статус sponsor (Спонсорбамбони)
            ItemStack sponsor = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/c1df70dc9885e418b58d8e7486a81ed4ec2e7963c42569c468084d4f567c6", 1)).build();
            this.inv.setItem(33, Items.name(sponsor, "&fПривилегия &d&lСпонсорбамбони &f- &a&l299 Руб.",
                    "",
                    " &d• &fПрефикс в чат и таб: &7«&d&lСпонсорбамбони&7» &7" + player.getDisplayName(),
                    " &d• &fПример сообщения в чате:",
                    "&7«&d&lСпонсорбамбони&7» &7" + player.getDisplayName() + " &8→ &7Hello World!",
                    "",
                    " &d◆ &fВозможность забанить самых настырных: &d/ban 30m",
                    "",
                    " &c▪ Донат выдается на &c&lвсе &cсервера.",
                    " &6▪ Возможности &6&lпредыдущих &6донатов.",
                    " &a▪ Донат &a&lостается &aпосле вайпа.",
                    " &f▪ Вы получите &b&l&n600&9&l Flex&f&lCoin &fза покупку",
                    "",
                    " &c&lПримечание: в данной привилегии присутствуют права,",
                    " &c&lЗа неправильное использование которых можно получить",
                    " &c&lнаказание. Вы несёте ответственность за свои действия.",
                    "",
                    "&fПокупать донат на сайте: &3www.FlexingWorld.net"));

            // Статус owner (Чикивладелец)
            ItemStack owner = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/6c92e3f45b49e405670224892f93ebc84fa7f8c96c36aab24a8854f2cbf0b8", 1)).build();
            this.inv.setItem(34, Items.name(owner, "&fПривилегия &6&lЧикивладелец &f- &a&l449 Руб.",
                    "",
                    " &6• &fПрефикс в чат и таб: &7«&6&lЧикивладелец&7» &7" + player.getDisplayName(),
                    " &6• &fПример сообщения в чате:",
                    "&7«&6&lЧикивладелец&7» &7" + player.getDisplayName() + " &8→ &7Hello World!",
                    "",
                    "",
                    " &6◆ &fУвеличено допустимое время для бана игрока: &6/ban 2h",
                    "",
                    " &c▪ Донат выдается на &c&lвсе &cсервера.",
                    " &6▪ Возможности &6&lпредыдущих &6донатов.",
                    " &a▪ Донат &a&lостается &aпосле вайпа.",
                    " &f▪ Вы получите &b&l&n900&9&l Flex&f&lCoin &fза покупку",
                    "",
                    " &c&lПримечание: в данной привилегии присутствуют права,",
                    " &c&lЗа неправильное использование которых можно получить",
                    " &c&lнаказание. Вы несёте ответственность за свои действия.",
                    "",
                    "&fПокупать донат на сайте: &ewww.FlexingWorld.net"));

            // Статус bombaster (Чикибомбастер)
            ItemStack bombaster = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/b88fb6b2b3efa6ab299f9f4e7d8c1a1d7338753bdf8fef81075f2155943bc69", 1)).build();
            this.inv.setItem(41, Items.name(bombaster, "&fПривилегия &c&lЧикибомбастер &f- &a&l699 Руб.",
                    "",
                    " &c• &fПрефикс в чат и таб: &7«&c&lЧикибомбастер&7» &f" + player.getDisplayName(),
                    " &c• &fПример сообщения в чате:",
                    "&7«&c&lЧикибомбастер&7» &f" + player.getDisplayName() + " &8→ &7Hello World!",
                    "",
                    " &c◆ &fУвеличено допустимое время для бана игрока: &c/ban 1week",
                    "",
                    " &c▪ Донат выдается на &c&lвсе &cсервера.",
                    " &6▪ Возможности &6&lпредыдущих &6донатов.",
                    " &a▪ Донат &a&lостается &aпосле вайпа.",
                    " &f▪ Вы получите &b&l&n1500&9&l Flex&f&lCoin &fза покупку",
                    "",
                    "&fПокупать донат на сайте: &3www.FlexingWorld.net"));

            // Статус god (Чикибамбог)
            ItemStack god = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/b88fb6b2b3efa6ab299f9f4e7d8c1a1d7338753bdf8fef81075f2155943bc69", 1)).build();
            this.inv.setItem(42, Items.name(god, "&fПривилегия &4&lЧикибамбог &f- &a&l2499 Руб.",
                    "",
                    " &4• &fПрефикс в чат и таб: &7«&4&lЧикибамбог&7» &7" + player.getDisplayName(),
                    " &4• &fПример сообщения в чате:",
                    "&7«&4&lЧикибамбог&7» &7" + player.getDisplayName() + " &8→ &7Hello World!",
                    " &4• &fВключён &4&n&lиммунитет&f от бана/кика &7(но Админ сервера сможет забанить)",
                    "",
                    "&c&lВоистину королевская привилегия для олигархичей",
                    "&c&lЛучшее решение на сервере, которое вы можете себе позволить",
                    "",
                    " &4◆ &4&n&lЭКСКЛЮЗИВ:&f доступ к Бану по IP на 30 минут",
                    " &4◆ &4&n&lЭКСКЛЮЗИВ:&f доступ к Муту на 10 часов",
                    "",
                    " &4◆ &fДоступ к вершине этого сервера",
                    " &4◆ &fВыше тебя будут только Админы",
                    " &4◆ &c&lЛучший донат для тех, кто хочет вписать своё имя в историю сервера",
                    " &4◆ &4&lСтань ебучей легендой Найт-сити!",
                    "",
                    " &c▪ Донат выдается на &c&lвсе &cсервера.",
                    " &6▪ Возможности &6&lпредыдущих &6донатов.",
                    " &a▪ Донат &a&lостается &aпосле вайпа.",
                    " &f▪ Вы получите &b&l&n5000&9&l Flex&f&lCoin &fза покупку",
                    "",
                    " &c&lПримечание: в данной привилегии присутствуют права,",
                    " &c&lЗа неправильное использование которых можно получить",
                    " &c&lнаказание. Вы несёте ответственность за свои действия.",
                    "",
                    "&fПокупать донат на сайте: &3www.FlexingWorld.net"));

            // Информация
            ItemStack information = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/9c96be7886eb7df75525a363e5f549626c21388f0fda988a6e8bf487a53", 1)).build();
            this.inv.setItem(43, Items.name(information, "&2&lИнформация", "", "&fЧтобы приобрести привилегию, нужно выполнить простой алгоритм:",
                    "&a• &fПерейти на сайт для покупки доната: &c&lFlexingWorld.net", "&a• &fВвести в первое поле свой игровой никнейм",
                    "&a• &fВыбрать привилегию во втором поле", "&a• &fПерейти по кнопке &aкупить &fна форму оплаты", "",
                    "&4&l[!] &fДонат привилегия будет выдана", "    &fмоментально и автоматически после покупки доната!", "",
                    "&eСайт покупки доната", "&ehttps:// &fFlexingWorld.net"));
        }

        @Override
        public void onClick(ItemStack is, Player player, int slot, ClickType clicktype) {
            switch (slot) {
                case 8:
                case 10:
                    player.openInventory(new GroupsMenu(player).getInventory());
                    break;
                case 11:
                    player.openInventory(new AnarchyMenu(player).getInventory());
                    break;
                case 14:
                    player.openInventory(new MoneyMenu(player).getInventory());
                    break;
                case 15:
                    player.openInventory(new CautionMenu(player).getInventory());
                    break;
                case 16:
                    player.openInventory(new AdminsMenu(player).getInventory());
                    break;
                case 40:
                    player.openInventory(new FlexCoinsMenu(player).getInventory());
                    break;
            }
        }

        @Override
        public Inventory getInventory() {
            return this.inv;
        }
    }

    private static class AnarchyMenu implements InvMenu {
        private static final Set<Integer> GLASS_PANE_SLOT = ImmutableSet.of(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(9), Integer.valueOf(13), Integer.valueOf(17), Integer.valueOf(18), Integer.valueOf(19), Integer.valueOf(20), Integer.valueOf(21), Integer.valueOf(22), Integer.valueOf(23), Integer.valueOf(24), Integer.valueOf(25), Integer.valueOf(26), Integer.valueOf(27), Integer.valueOf(31), Integer.valueOf(35), Integer.valueOf(36), Integer.valueOf(44), Integer.valueOf(45), Integer.valueOf(46), Integer.valueOf(47), Integer.valueOf(48), Integer.valueOf(49), Integer.valueOf(50), Integer.valueOf(51), Integer.valueOf(52), Integer.valueOf(53));
        private final Inventory inv;

        public AnarchyMenu(Player player) {
            this.inv = Bukkit.createInventory(this, 54, "Донат привилегии | Анархия");

            ItemStack GLASS_PANE = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
            ItemMeta GLASS_PANE_META = GLASS_PANE.getItemMeta();
            GLASS_PANE_META.setDisplayName("§6§k|§a§k|§e§k|§c§k|");
            GLASS_PANE.setItemMeta(GLASS_PANE_META);
            this.GLASS_PANE_SLOT.forEach(slot -> this.inv.setItem(slot.intValue(), GLASS_PANE));

            this.inv.setItem(8, Items.name(new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/647cf0f3b9ec9df2485a9cd4795b60a391c8e6ebac96354de06e3357a9a88607", 1)).build(), "&3&lНа главную"));
            this.inv.setItem(10, Items.name(Material.GRASS, "&f&lВозможности на &a&lВыживании"));
            this.inv.setItem(11, Items.name(Items.glow(Material.TNT), "&f&lВы просматриваете возможности на &c&lАнархии"));
            this.inv.setItem(12, Items.name(Material.IRON_SWORD, "&f&lВозможности на &b&lМини-играх"));
            this.inv.setItem(14, Items.name(Material.QUARTZ, "&e&lКуда уйдут деньги с покупки?", "&7Прочитайте этот пункт", "&7Если вам интересно", "&7Что произойдёт с вашими деньгами"));
            this.inv.setItem(15, Items.name(Items.glow(Material.REDSTONE), "&c&lОбратите внимание", "&7Прочитайте этот пункт", "&7И узнайте об особенностях покупки", "&7Которые нужно учитывать"));
            this.inv.setItem(16, Items.name(Material.MAGMA_CREAM, "&a&lВозможности Админов", "&7Рекомендуется прочитать этот пункт", "&7Чтобы узнать больше об Админах", "&7И их возможностях"));
            this.inv.setItem(40, Items.name(Items.glow(Material.ENDER_PEARL), "&9&lFlex&f&lCoins"));

            // Статус VIP (Чикибамбвипка)
            ItemStack vip = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/595aca72dcce1298475398fb7adff8447c9a670e6cf64dd3933bbc94918e99", 1)).build();

            this.inv.setItem(28, Items.name(vip, "&fПривилегия &a&lЧикибамбвипка &f- &a&l29 Руб.&f/мес.",
                    "",
                    " &a• &fПрефикс в чат и таб: &7«&a&lЧикибамбвипка&7» &7" + player.getDisplayName(),
                    " &a• &fПример сообщения в чате:",
                    "&7«&a&lЧикибамбвипка&7» &7" + player.getDisplayName() + " &8→ &7Hello World!",
                    "",
                    "&c&lУ данной привилегии прав на анархии нет!",
                    "",
                    " &c▪ Донат выдается на &c&lвсе &cсервера.",
                    " &a▪ Донат &a&lостается &aпосле вайпа.",
                    " &f▪ Вы получите &b&l&n60&9&l Flex&f&lCoin &fза покупку",
                    "",
                    "&fПокупать донат на сайте: &3www.FlexingWorld.net"));

            // Статус premium (Премиумбамбони)
            ItemStack premium = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/595aca72dcce1298475398fb7adff8447c9a670e6cf64dd3933bbc94918e99", 1)).build();
            this.inv.setItem(29, Items.name(premium, "&fПривилегия &5&lПремиумбамбони &f- &a&l49 Руб.&f/мес.",
                    "",
                    " &5• &fПрефикс в чат и таб: &7«&5&lПремиумбамбони&7» &7" + player.getDisplayName(),
                    " &5• &fПример сообщения в чате:",
                    "&7«&5&lПремиумбамбони&7» &7" + player.getDisplayName() + " &8→ &7Hello World!",
                    "",
                    "&c&lУ данной привилегии прав на анархии нет!",
                    "",
                    " &c▪ Донат выдается на &c&lвсе &cсервера.",
                    " &6▪ Возможности &6&lпредыдущих &6донатов.",
                    " &a▪ Донат &a&lостается &aпосле вайпа.",
                    " &f▪ Вы получите &b&l&n100&9&l Flex&f&lCoin &fза покупку",
                    "",
                    "&fПокупать донат на сайте: &3www.FlexingWorld.net"));

            // Статус creative (Чикикреатив)
            ItemStack creative = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/595aca72dcce1298475398fb7adff8447c9a670e6cf64dd3933bbc94918e99", 1)).build();
            this.inv.setItem(30, Items.name(creative, "&fПривилегия &9&lЧикикреатив &f- &a&l69 Руб.&f/мес.",
                    "",
                    " &9• &fПрефикс в чат и таб: &7«&9&lЧикикреатив&7» &7" + player.getDisplayName(),
                    " &9• &fПример сообщения в чате:",
                    "&7«&9&lЧикикреатив&7» &7" + player.getDisplayName() + " &8→ &7Hello World!",
                    "",
                    "&c&lУ данной привилегии прав на анархии нет!",
                    "",
                    " &c▪ Донат выдается на &c&lвсе &cсервера.",
                    " &6▪ Возможности &6&lпредыдущих &6донатов.",
                    " &a▪ Донат &a&lостается &aпосле вайпа.",
                    " &f▪ Вы получите &b&l&n130&9&l Flex&f&lCoin &fза покупку",
                    "",
                    "&fПокупать донат на сайте: &3www.FlexingWorld.net"));

            // Статус moder (Чикимодератор)
            ItemStack moder = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/595aca72dcce1298475398fb7adff8447c9a670e6cf64dd3933bbc94918e99", 1)).build();
            this.inv.setItem(37, Items.name(moder, "&fПривилегия &e&lЧикимодератор &f- &a&l99 Руб.",
                    "",
                    " &e• &fПрефикс в чат и таб: &7«&e&lЧикимодератор&7»  &7" + player.getDisplayName(),
                    " &e• &fПример сообщения в чате:",
                    "&7«&e&lЧикимодератор&7» &7" + player.getDisplayName() + " &8→ &7Hello World!",
                    "",
                    "&c&lУ данной привилегии прав на анархии нет!",
                    "",
                    " &c▪ Донат выдается на &c&lвсе &cсервера.",
                    " &6▪ Возможности &6&lпредыдущих &6донатов.",
                    " &a▪ Донат &a&lостается &aпосле вайпа.",
                    " &f▪ Вы получите &b&l&n200&9&l Flex&f&lCoin &fза покупку",
                    "",
                    "&fПокупать донат на сайте: &3www.FlexingWorld.net"));

            // Статус chikibamboni (Чикибамбони)
            ItemStack chikibamboni = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/595aca72dcce1298475398fb7adff8447c9a670e6cf64dd3933bbc94918e99", 1)).build();
            this.inv.setItem(38, Items.name(chikibamboni, "&fПривилегия &3&lЧикибамбони &f- &a&l129 Руб.",
                    "",
                    " &3• &fПрефикс в чат и таб: &7«&3&lЧикибамбони&7» &7" + player.getDisplayName(),
                    " &3• &fПример сообщения в чате:",
                    "&7«&3&lЧикибамбони&7» &7" + player.getDisplayName() + " &8→ &7Hello World!",
                    "",
                    "&c&lУ данной привилегии прав на анархии нет!",
                    "",
                    " &c▪ Донат выдается на &c&lвсе &cсервера.",
                    " &6▪ Возможности &6&lпредыдущих &6донатов.",
                    " &a▪ Донат &a&lостается &aпосле вайпа.",
                    " &f▪ Вы получите &b&l&n250&9&l Flex&f&lCoin &fза покупку",
                    "",
                    "&fПокупать донат на сайте: &3www.FlexingWorld.net"));

            // Статус admin (Админобамбони)
            ItemStack admin = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/3e427adff356f0945ebba391ca0eef465ae17461ea7c53ad173fbb6c7f36a8", 1)).build();
            this.inv.setItem(39, Items.name(admin, "&fПривилегия &b&lАдминобамбони &f- &a&l179 Руб.",
                    "",
                    " &b• &fПрефикс в чат и таб: &7«&b&lАдминобамбони&7» &7" + player.getDisplayName(),
                    " &b• &fПример сообщения в чате:",
                    "&7«&b&lАдминобамбони&7» &7" + player.getDisplayName() + " &8→ &7Hello World!",
                    "",
                    " &b◆ &fВозможность изменить ник: &b/nick [новый ник]",
                    "",
                    " &c▪ Донат выдается на &c&lвсе &cсервера.",
                    " &6▪ Возможности &6&lпредыдущих &6донатов.",
                    " &a▪ Донат &a&lостается &aпосле вайпа.",
                    " &f▪ Вы получите &b&l&n360&9&l Flex&f&lCoin &fза покупку",
                    "",
                    "&fПокупать донат на сайте: &3www.FlexingWorld.net"));

            // Статус chikybambonylla (Чикибамбонила)
            ItemStack chikybambonylla = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/58b3ffd37e6f32a4e7c1aba2cb484923cf695f447c879e712ec25a613e010", 1)).build();
            this.inv.setItem(32, Items.name(chikybambonylla, "&fПривилегия &2&lЧикибамбонила &f- &a&l219 Руб.",
                    "",
                    " &2• &fПрефикс в чат и таб: &7«&2&lЧикибамбонила&7» &7" + player.getDisplayName(),
                    " &2• &fПример сообщения в чате:",
                    "&7«&2&lЧикибамбонила&7» &7" + player.getDisplayName() + " &8→ &7Hello World!",
                    "",
                    " &2◆ &fВозможность кикнуть недоброжелателя: &2/kick [ник игрока]",
                    " &2◆ &fВозможность писать цветным текстом в чате",
                    " &2• &fПример сообщения в чате:",
                    " &7«&2&lЧикибамбонила&7» &7" + player.getDisplayName() + " &8→ &3Fisting &5ass",
                    "",
                    " &c▪ Донат выдается на &c&lвсе &cсервера.",
                    " &6▪ Возможности &6&lпредыдущих &6донатов.",
                    " &a▪ Донат &a&lостается &aпосле вайпа.",
                    " &f▪ Вы получите &b&l&n450&9&l Flex&f&lCoin &fза покупку",
                    "",
                    " &c&lПримечание: в данной привилегии присутствуют права,",
                    " &c&lЗа неправильное использование которых можно получить",
                    " &c&lнаказание. Вы несёте ответственность за свои действия.",
                    "",
                    "&fПокупать донат на сайте: &3www.FlexingWorld.net"));

            // Статус sponsor (Спонсорбамбони)
            ItemStack sponsor = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/c1df70dc9885e418b58d8e7486a81ed4ec2e7963c42569c468084d4f567c6", 1)).build();
            this.inv.setItem(33, Items.name(sponsor, "&fПривилегия &d&lСпонсорбамбони &f- &a&l299 Руб.",
                    "",
                    " &d• &fПрефикс в чат и таб: &7«&d&lСпонсорбамбони&7» &7" + player.getDisplayName(),
                    " &d• &fПример сообщения в чате:",
                    "&7«&d&lСпонсорбамбони&7» &7" + player.getDisplayName() + " &8→ &7Hello World!",
                    "",
                    " &d◆ &fВозможность забанить самых настырных: &d/ban 30m",
                    "",
                    " &c▪ Донат выдается на &c&lвсе &cсервера.",
                    " &6▪ Возможности &6&lпредыдущих &6донатов.",
                    " &a▪ Донат &a&lостается &aпосле вайпа.",
                    " &f▪ Вы получите &b&l&n600&9&l Flex&f&lCoin &fза покупку",
                    "",
                    " &c&lПримечание: в данной привилегии присутствуют права,",
                    " &c&lЗа неправильное использование которых можно получить",
                    " &c&lнаказание. Вы несёте ответственность за свои действия.",
                    "",
                    "&fПокупать донат на сайте: &3www.FlexingWorld.net"));

            // Статус owner (Чикивладелец)
            ItemStack owner = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/6c92e3f45b49e405670224892f93ebc84fa7f8c96c36aab24a8854f2cbf0b8", 1)).build();
            this.inv.setItem(34, Items.name(owner, "&fПривилегия &6&lЧикивладелец &f- &a&l449 Руб.",
                    "",
                    " &6• &fПрефикс в чат и таб: &7«&6&lЧикивладелец&7» &7" + player.getDisplayName(),
                    " &6• &fПример сообщения в чате:",
                    "&7«&6&lЧикивладелец&7» &7" + player.getDisplayName() + " &8→ &7Hello World!",
                    "",
                    " &6◆ &fУвеличено допустимое время для бана игрока: &6/ban 2h",
                    "",
                    " &c▪ Донат выдается на &c&lвсе &cсервера.",
                    " &6▪ Возможности &6&lпредыдущих &6донатов.",
                    " &a▪ Донат &a&lостается &aпосле вайпа.",
                    " &f▪ Вы получите &b&l&n900&9&l Flex&f&lCoin &fза покупку",
                    "",
                    " &c&lПримечание: в данной привилегии присутствуют права,",
                    " &c&lЗа неправильное использование которых можно получить",
                    " &c&lнаказание. Вы несёте ответственность за свои действия.",
                    "",
                    "&fПокупать донат на сайте: &3www.FlexingWorld.net"));

            // Статус bombaster (Чикибомбастер)
            ItemStack bombaster = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/b88fb6b2b3efa6ab299f9f4e7d8c1a1d7338753bdf8fef81075f2155943bc69", 1)).build();
            this.inv.setItem(41, Items.name(bombaster, "&fПривилегия &c&lЧикибомбастер &f- &a&l699 Руб.",
                    "",
                    " &c• &fПрефикс в чат и таб: &7«&c&lЧикибомбастер&7» &f" + player.getDisplayName(),
                    " &c• &fПример сообщения в чате:",
                    "&7«&c&lЧикибомбастер&7» &f" + player.getDisplayName() + " &8→ &7Hello World!",
                    "",
                    " &c◆ &fУвеличено допустимое время для бана игрока: &c/ban 1week",
                    "",
                    " &c▪ Донат выдается на &c&lвсе &cсервера.",
                    " &6▪ Возможности &6&lпредыдущих &6донатов.",
                    " &a▪ Донат &a&lостается &aпосле вайпа.",
                    " &f▪ Вы получите &b&l&n1500&9&l Flex&f&lCoin &fза покупку",
                    "",
                    "&fПокупать донат на сайте: &3www.FlexingWorld.net"));

            // Статус god (Чикибомбастер)
            ItemStack god = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/b88fb6b2b3efa6ab299f9f4e7d8c1a1d7338753bdf8fef81075f2155943bc69", 1)).build();
            this.inv.setItem(42, Items.name(god, "&fПривилегия &4&lЧикибамбог &f- &a&l2499 Руб.",
                    "",
                    " &4• &fПрефикс в чат и таб: &7«&4&lЧикибамбог&7» &7" + player.getDisplayName(),
                    " &4• &fПример сообщения в чате:",
                    "&7«&4&lЧикибамбог&7» &7" + player.getDisplayName() + " &8→ &7Hello World!",
                    " &4• &fВключён &4&n&lиммунитет&f от бана/кика &7(но Админ сервера сможет забанить)",
                    "",
                    "&c&lВоистину королевская привилегия для олигархичей",
                    "&c&lЛучшее решение на сервере, которое вы можете себе позволить",
                    "",
                    " &4◆ &4&n&lЭКСКЛЮЗИВ:&f доступ к Бану по IP на 30 минут",
                    " &4◆ &4&n&lЭКСКЛЮЗИВ:&f доступ к Муту на 10 часов",
                    "",
                    " &4◆ &fДоступ к вершине этого сервера",
                    " &4◆ &fВыше тебя будут только Админы",
                    " &4◆ &c&lЛучший донат для тех, кто хочет вписать своё имя в историю сервера",
                    " &4◆ &4&lСтань ебучей легендой Найт-сити!",
                    "",
                    " &c▪ Донат выдается на &c&lвсе &cсервера.",
                    " &6▪ Возможности &6&lпредыдущих &6донатов.",
                    " &a▪ Донат &a&lостается &aпосле вайпа.",
                    " &f▪ Вы получите &b&l&n5000&9&l Flex&f&lCoin &fза покупку",
                    "",
                    " &c&lПримечание: в данной привилегии присутствуют права,",
                    " &c&lЗа неправильное использование которых можно получить",
                    " &c&lнаказание. Вы несёте ответственность за свои действия.",
                    "",
                    "&fПокупать донат на сайте: &3www.FlexingWorld.net"));

            // Информация
            ItemStack information = new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/9c96be7886eb7df75525a363e5f549626c21388f0fda988a6e8bf487a53", 1)).build();
            this.inv.setItem(43, Items.name(information, "&2&lИнформация", "", "&fЧтобы приобрести привилегию, нужно выполнить простой алгоритм:",
                    "&a• &fПерейти на сайт для покупки доната: &c&lFlexingWorld.net", "&a• &fВвести в первое поле свой игровой никнейм",
                    "&a• &fВыбрать привилегию во втором поле", "&a• &fПерейти по кнопке &aкупить &fна форму оплаты", "",
                    "&4&l[!] &fДонат привилегия будет выдана", "    &fмоментально и автоматически после покупки доната!", "",
                    "&eСайт покупки доната", "&ehttps:// &fFlexingWorld.net"));
        }

        @Override
        public void onClick(ItemStack is, Player player, int slot, ClickType clicktype) {
            switch (slot) {
                case 8:
                case 10:
                    player.openInventory(new GroupsMenu(player).getInventory());
                    break;
                case 12:
                    player.openInventory(new MiniGamesGroups(player).getInventory());
                    break;
                case 14:
                    player.openInventory(new MoneyMenu(player).getInventory());;
                    break;
                case 15:
                    player.openInventory(new CautionMenu(player).getInventory());
                    break;
                case 16:
                    player.openInventory(new AdminsMenu(player).getInventory());
                    break;
                case 40:
                    player.openInventory(new FlexCoinsMenu(player).getInventory());
                    break;
            }
        }

        @Override
        public Inventory getInventory() {
            return this.inv;
        }
    }

    private static class MoneyMenu implements InvMenu {
        private static final Set<Integer> GLASS_PANE_SLOT = ImmutableSet.of(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(9), Integer.valueOf(13), Integer.valueOf(17), Integer.valueOf(18), Integer.valueOf(19), Integer.valueOf(20), Integer.valueOf(21), Integer.valueOf(23), Integer.valueOf(24), Integer.valueOf(25), Integer.valueOf(26), Integer.valueOf(27), Integer.valueOf(28), Integer.valueOf(29), Integer.valueOf(30), Integer.valueOf(31), Integer.valueOf(32), Integer.valueOf(33), Integer.valueOf(34), Integer.valueOf(35), Integer.valueOf(36), Integer.valueOf(37), Integer.valueOf(39), Integer.valueOf(41), Integer.valueOf(42), Integer.valueOf(43), Integer.valueOf(44), Integer.valueOf(45), Integer.valueOf(46), Integer.valueOf(47), Integer.valueOf(48), Integer.valueOf(49), Integer.valueOf(50), Integer.valueOf(51), Integer.valueOf(52), Integer.valueOf(53));
        private final Inventory inv;

        public MoneyMenu(Player player) {
            this.inv = Bukkit.createInventory(this, 54, "Куда уйдут деньги с покупки?");

            ItemStack GLASS_PANE = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
            ItemMeta GLASS_PANE_META = GLASS_PANE.getItemMeta();
            GLASS_PANE_META.setDisplayName("§6§k|§a§k|§e§k|§c§k|");
            GLASS_PANE.setItemMeta(GLASS_PANE_META);
            this.GLASS_PANE_SLOT.forEach(slot -> this.inv.setItem(slot.intValue(), GLASS_PANE));

            this.inv.setItem(8, Items.name(new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/647cf0f3b9ec9df2485a9cd4795b60a391c8e6ebac96354de06e3357a9a88607", 1)).build(), "&3&lНа главную"));
            this.inv.setItem(10, Items.name(Material.GRASS, "&f&lВозможности на &a&lВыживании"));
            this.inv.setItem(11, Items.name(Material.TNT, "&f&lВозможности на &c&lАнархии"));
            this.inv.setItem(12, Items.name(Material.IRON_SWORD, "&f&lВозможности на &b&lМини-играх"));
            this.inv.setItem(14, Items.name(Items.glow(Material.QUARTZ), "&e&lКуда уйдут деньги с покупки?", "&7Прочитайте этот пункт", "&7Если вам интересно", "&7Что произойдёт с вашими деньгами"));
            this.inv.setItem(15, Items.name(Items.glow(Material.REDSTONE), "&c&lОбратите внимание", "&7Прочитайте этот пункт", "&7И узнайте об особенностях покупки", "&7Которые нужно учитывать"));
            this.inv.setItem(16, Items.name(Material.MAGMA_CREAM, "&a&lВозможности Админов", "&7Рекомендуется прочитать этот пункт", "&7Чтобы узнать больше об Админах", "&7И их возможностях"));
            this.inv.setItem(22, Items.name(Items.glow(Material.BOOK), "&b&lКуда уйдут ваши деньги?", "&fБудьте спокойны за свои деньги", "&fОни попадут в хорошие руки"));
            this.inv.setItem(38, Items.name(new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/82cde068e99a4f98c31f87b4cc06be14b229aca4f7281a416c7e2f553223db74", 1)).build(), "&e&l№1. &fРазвитие сервера", "&fНа сервер тоже требуются деньги!", "&fОплата хостинга, техническое обслуживание", "&fРазработка новых режимов — всё это требует денег"));
            this.inv.setItem(40, Items.name(new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/82cde068e99a4f98c31f87b4cc06be14b229aca4f7281a416c7e2f553223db74", 1)).build(), "&e&l№2. &fСпонсирование мероприятий", "&fВремя от времени проводятся состязания по BedWars и строительству", "&fПобедители получают денежное вознаграждение!", "&fВсе деньги берутся из бюджета нашего сервера"));
            this.inv.setItem(42, Items.name(new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/82cde068e99a4f98c31f87b4cc06be14b229aca4f7281a416c7e2f553223db74", 1)).build(), "&e&l№3. &fАдминам и модераторам", "&fАдмины тоже люди!", "&fКаждый из них имеет право получать вознаграждение", "&fЗа все свои труды и силы, которые они вкладывают в сервер"));
        }

        @Override
        public void onClick(ItemStack is, Player player, int slot, ClickType clicktype) {
            switch (slot) {
                case 8:
                case 10:
                    player.openInventory(new GroupsMenu(player).getInventory());
                    break;
                case 11:
                    player.openInventory(new AnarchyMenu(player).getInventory());
                    break;
                case 12:
                    player.openInventory(new MiniGamesGroups(player).getInventory());
                    break;
                case 15:
                    player.openInventory(new CautionMenu(player).getInventory());
                    break;
                case 16:
                    player.openInventory(new AdminsMenu(player).getInventory());
                    break;
            }
        }

        @Override
        public Inventory getInventory() {
            return this.inv;
        }
    }

    private static class CautionMenu implements InvMenu {
        private static final Set<Integer> GLASS_PANE_SLOT = ImmutableSet.of(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(9), Integer.valueOf(13), Integer.valueOf(17), Integer.valueOf(18), Integer.valueOf(19), Integer.valueOf(20), Integer.valueOf(21), Integer.valueOf(23), Integer.valueOf(24), Integer.valueOf(25), Integer.valueOf(26), Integer.valueOf(27), Integer.valueOf(28), Integer.valueOf(29), Integer.valueOf(30), Integer.valueOf(31), Integer.valueOf(32), Integer.valueOf(33), Integer.valueOf(34), Integer.valueOf(35), Integer.valueOf(36), Integer.valueOf(37), Integer.valueOf(39), Integer.valueOf(41), Integer.valueOf(42), Integer.valueOf(43), Integer.valueOf(44), Integer.valueOf(45), Integer.valueOf(46), Integer.valueOf(47), Integer.valueOf(48), Integer.valueOf(49), Integer.valueOf(50), Integer.valueOf(51), Integer.valueOf(52), Integer.valueOf(53));
        private final Inventory inv;

        public CautionMenu(Player player) {
            this.inv = Bukkit.createInventory(this, 54, "Обратите внимание");

            ItemStack GLASS_PANE = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
            ItemMeta GLASS_PANE_META = GLASS_PANE.getItemMeta();
            GLASS_PANE_META.setDisplayName("§6§k|§a§k|§e§k|§c§k|");
            GLASS_PANE.setItemMeta(GLASS_PANE_META);
            this.GLASS_PANE_SLOT.forEach(slot -> this.inv.setItem(slot.intValue(), GLASS_PANE));

            this.inv.setItem(8, Items.name(new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/647cf0f3b9ec9df2485a9cd4795b60a391c8e6ebac96354de06e3357a9a88607", 1)).build(), "&3&lНа главную"));
            this.inv.setItem(10, Items.name(Material.GRASS, "&f&lВозможности на &a&lВыживании"));
            this.inv.setItem(11, Items.name(Material.TNT, "&f&lВозможности на &c&lАнархии"));
            this.inv.setItem(12, Items.name(Material.IRON_SWORD, "&f&lВозможности на &b&lМини-играх"));
            this.inv.setItem(14, Items.name(Material.QUARTZ, "&e&lКуда уйдут деньги с покупки?", "&7Прочитайте этот пункт", "&7Если вам интересно", "&7Что произойдёт с вашими деньгами"));
            this.inv.setItem(15, Items.name(Items.glow(Material.REDSTONE), "&c&lОбратите внимание", "&7Прочитайте этот пункт", "&7И узнайте об особенностях покупки", "&7Которые нужно учитывать"));
            this.inv.setItem(16, Items.name(Material.MAGMA_CREAM, "&a&lВозможности Администрации", "&7Рекомендуется прочитать этот пункт", "&7Чтобы узнать больше об Администрации", "&7И их возможностях"));
            this.inv.setItem(22, Items.name(Items.glow(Material.BOOK), "&b&lПредупреждения", "&fЭто три главных условия приобретения доната"));
            this.inv.setItem(38, Items.name(new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/e7f9c6fef2ad96b3a5465642ba954671be1c4543e2e25e56aef0a47d5f1f", 1)).build(), "&c&l№1", "&fПокупая любую привилегию", "&fВы подтверждаете своё согласие", "&fС &a&lнашими правилами"));
            this.inv.setItem(40, Items.name(new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/e7f9c6fef2ad96b3a5465642ba954671be1c4543e2e25e56aef0a47d5f1f", 1)).build(), "&c&l№2", "&fВозврат средств за купленную привилегию", "&c&lНе осуществляется &fни при каких обстоятельствах"));
            this.inv.setItem(42, Items.name(new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/e7f9c6fef2ad96b3a5465642ba954671be1c4543e2e25e56aef0a47d5f1f", 1)).build(), "&c&l№3", "&fЕсли вы совершите серьёзное нарушение", "&fАдмины &c&lвправе &fотобрать у вас привилегию", "&fИмейте это ввиду, используйте свои возможности с умом"));
        }

        @Override
        public void onClick(ItemStack is, Player player, int slot, ClickType clicktype) {
            switch (slot) {
                case 8:
                case 10:
                    player.openInventory(new GroupsMenu(player).getInventory());
                    break;
                case 11:
                    player.openInventory(new AnarchyMenu(player).getInventory());
                    break;
                case 12:
                    player.openInventory(new MiniGamesGroups(player).getInventory());
                    break;
                case 14:
                    player.openInventory(new MoneyMenu(player).getInventory());;
                    break;
                case 16:
                    player.openInventory(new AdminsMenu(player).getInventory());
                    break;
            }
        }

        @Override
        public Inventory getInventory() {
            return this.inv;
        }
    }

    private static class AdminsMenu implements InvMenu {
        private static final Set<Integer> GLASS_PANE_SLOT = ImmutableSet.of(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6), Integer.valueOf(7), Integer.valueOf(9), Integer.valueOf(13), Integer.valueOf(17), Integer.valueOf(18), Integer.valueOf(19), Integer.valueOf(20), Integer.valueOf(21), Integer.valueOf(23), Integer.valueOf(24), Integer.valueOf(25), Integer.valueOf(26), Integer.valueOf(27), Integer.valueOf(29), Integer.valueOf(31), Integer.valueOf(33), Integer.valueOf(35), Integer.valueOf(36), Integer.valueOf(37), Integer.valueOf(38), Integer.valueOf(39), Integer.valueOf(40), Integer.valueOf(41), Integer.valueOf(42), Integer.valueOf(43), Integer.valueOf(44), Integer.valueOf(45), Integer.valueOf(46), Integer.valueOf(47), Integer.valueOf(48), Integer.valueOf(49), Integer.valueOf(50), Integer.valueOf(51), Integer.valueOf(52), Integer.valueOf(53));
        private final Inventory inv;

        public AdminsMenu(Player player) {
            this.inv = Bukkit.createInventory(this, 54, "Возможности Админов");

            ItemStack GLASS_PANE = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
            ItemMeta GLASS_PANE_META = GLASS_PANE.getItemMeta();
            GLASS_PANE_META.setDisplayName("§6§k|§a§k|§e§k|§c§k|");
            GLASS_PANE.setItemMeta(GLASS_PANE_META);
            this.GLASS_PANE_SLOT.forEach(slot -> this.inv.setItem(slot.intValue(), GLASS_PANE));

            this.inv.setItem(8, Items.name(new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/647cf0f3b9ec9df2485a9cd4795b60a391c8e6ebac96354de06e3357a9a88607", 1)).build(), "&3&lНа главную"));
            this.inv.setItem(10, Items.name(Material.GRASS, "&f&lВозможности на &a&lВыживании"));
            this.inv.setItem(11, Items.name(Material.TNT, "&f&lВозможности на &c&lАнархии"));
            this.inv.setItem(12, Items.name(Material.IRON_SWORD, "&f&lВозможности на &b&lМини-играх"));
            this.inv.setItem(14, Items.name(Material.QUARTZ, "&e&lКуда уйдут деньги с покупки?", "&7Прочитайте этот пункт", "&7Если вам интересно", "&7Что произойдёт с вашими деньгами"));
            this.inv.setItem(15, Items.name(Items.glow(Material.REDSTONE), "&c&lОбратите внимание", "&7Прочитайте этот пункт", "&7И узнайте об особенностях покупки", "&7Которые нужно учитывать"));
            this.inv.setItem(16, Items.name(Items.glow(Material.MAGMA_CREAM), "&a&lВозможности Админов", "&7Рекомендуется прочитать этот пункт", "&7Чтобы узнать больше об Админах", "&7И их возможностях"));
            this.inv.setItem(22, Items.name(Items.glow(Material.BOOK), "&b&lПривилегии Администрации", "&fУвидели такого на сервере?", "&fЗначит стоит быть осторожнее", "&fЭти ребята могут дать наказание любому"));
            this.inv.setItem(28, Items.name(new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/39227fcadc3776d426e56b57882edcf9b08ea1ce8ba2a6da2cccac3e65b5", 1)).build(), "&9&lКибербамбонёнок", "&a&lНаши стажёры", "&fНовички в нашей команде", "&fИм даётся минимальный минимум команд Админов", "&fНо при этом они смогут забанить даже &4&lЧикибамбога", "&fЗато к ним достаточно строгое отношение", "&fАдмины внимательно следят за их действиями"));
            this.inv.setItem(30, Items.name(new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/fac5013b227a494ec16c4eafab1f84dea572efb2f3c4b5c3ff88252f356a02b", 1)).build(), "&4&lМладший кибербамбони", "&a&lТе, кто показали свои способности", "&fПолноправные Админы сервера", "&fКоторые имеют больше возможностей", "&fНежели наши стажёры", "&fИмеют гораздо более широкий инструментарий", "&fДля Администрирования"));
            this.inv.setItem(32, Items.name(new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/fac5013b227a494ec16c4eafab1f84dea572efb2f3c4b5c3ff88252f356a02b", 1)).build(), "&4&lСтарший кибербамбони", "&a&lВетераны, заслужившие наше доверие", "&fНа этой должности работают люди", "&fКоторые показали все свои лучшие качества", "&fИ внесли огромный вклад в развитие сервера", "&fУ них есть практически полный доступ к серверу", "&fИ они не ограничены в выдаче наказаний", "&7Разве что опки нет"));
            this.inv.setItem(34, Items.name(new ItemBuilder(SkullBuilder.getSkull("https://textures.minecraft.net/texture/fac5013b227a494ec16c4eafab1f84dea572efb2f3c4b5c3ff88252f356a02b", 1)).build(), "&4&lГлавный кибербамбони", "&c&lНастоящие прародители сервера", "&fЭто действительно влиятельные люди", "&fКоторые работали над сервером", "&fС самого его открытия", "&fУ них есть абсолютно полный доступ", "&fКо всем системам сервера", "&fА также опка", "&fС такими ребятами", "&fЛучше вообще не вступать в конфликты"));
        }

        @Override
        public void onClick(ItemStack is, Player player, int slot, ClickType clicktype) {
            switch (slot) {
                case 8:
                case 10:
                    player.openInventory(new GroupsMenu(player).getInventory());
                    break;
                case 11:
                    player.openInventory(new AnarchyMenu(player).getInventory());
                    break;
                case 12:
                    player.openInventory(new MiniGamesGroups(player).getInventory());
                    break;
                case 14:
                    player.openInventory(new MoneyMenu(player).getInventory());;
                    break;
                case 15:
                    player.openInventory(new CautionMenu(player).getInventory());
                    break;
            }
        }

        @Override
        public Inventory getInventory() {
            return this.inv;
        }
    }
}