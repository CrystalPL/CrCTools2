# Tools

Plugin dodaje podstawowe komendy na serwerze.

Do działania pluginu wymagane są następujące pluginy: **CrCAPICore, CrCAPIMessage**.
___

### Instalacja

Plugin przeznaczony jest dla wersji MC od 1.8 do 1.18.1. Działa z dowolnym silnikiem opartym o BukkitAPI — CraftBukkit,
Spigot, Paper, Tuinity itp.
___

### Komendy

W pluginie jest wiele komend, a poniżej ich lista.

| Komenda                           | Uprawnienie                                   | Opis komendy                      |
|-----------------------------------|-----------------------------------------------|-----------------------------------|
| /heal                             | crc.tools.heal                                | Leczy gracza                      |
| /heal \<gracz>                    | crc.tools.heal.player i crc.tools.heal.player | Leczy podanego gracza             |
| /feed                             | crc.tools.feed                                | Karmi gracza                      |
| /feed \<gracz>                    | crc.tools.feed i crc.tools.feed.player        | Karmi podanego gracza             |
| /gamemode <survival, 0>           | crc.tools.gamemode                            | Ustawia tryb przetrwania          |
| /gamemode <creative, 1>           | crc.tools.gamemode                            | Ustawia tryb kreatywny            |
| /gamemode <adventure, 2>          | crc.tools.gamemode                            | Ustawia tryb przygody             |
| /gamemode <spectator, 3>          | crc.tools.gamemode                            | Ustawia tryb obserwatora          |
| /gamemode <survival, 0> \<gracz>  | crc.tools.gamemode.player                     | Ustawia graczowi tryb przetrwania |
| /gamemode <creative, 1> \<gracz>  | crc.tools.gamemode.player                     | Ustawia graczowi tryb kreatywny   |
| /gamemode <adventure, 2> \<gracz> | crc.tools.gamemode.player                     | Ustawia graczowi tryb przygody    |
| /gamemode <spectator, 3> \<gracz> | crc.tools.gamemode.player                     | Ustawia graczowi tryb obserwatora |
| /rename \<nazwa>                  | crc.tools.rename                              | Zmienia nazwę przedmiotu          |
| /lore \<opis>                     | crc.tools.lore                                | Zmienia opis przedmiotu           |

___

# Anty-AFK

Plugin posiada podstawowe zabezpieczenie przed graczami, którzy afczą. Po określonym czasie afczenia może zostać
wywołana jakaś komenda lub gracz może dostać wiadomość na czacie. Jeśli gracz będzie miał
permisję `crc.tools.afk.bypass` nie będzie obowiązywał go zakaz afczenia.
___

# Informacje

* Jeśli gracz ma uprawnienie `crc.tools.cmdcooldown.bypass`, to nie obowiązuję go czas między wpisywanymi komendami.