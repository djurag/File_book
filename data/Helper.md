# Plava Knjiga helper
Ova aplikacija namijenjena je za uredsku uporabu za potrebe vođenja evidencije
o spisima. Aplikacija se sastoji od nekoliko segmenata koji će biti predstavljeni u
nastavku.
## Glavni zaslon
![Glavni zaslon](https://github.com/djurag/File_book/blob/master/data/Main_screen.png)

Sastoji se od više tipki kojima se određuje način rada. 

Pritiskom na tipku "Otvori" otvara se novi prozor u kojem je moguće pretraživati
datoteke na vašem računalu. Da bi program ispravno radio potrebno je odabrati
datoteku tipa Coma Separated Values (*.csv) koji sadrži zapise o spisima. Inicijalno
su ponuđene samo odgovarajuće datoteke.

![Otvori](https://github.com/djurag/File_book/blob/master/data/On_open_click.png)

Nakon odabira datoteke, neposredno pokraj gumba prikazuje se ime odabrane datoteke.
Učitavanje datoteke u tablicu izvršava se klikom na gumb "Učitaj datoteku" čime se
dobije sljedeći prikaz tablice:

![Ucitana tablica](https://github.com/djurag/File_book/blob/master/data/Refresh_table.png)

Tablica sadrži sve zapise o imenima spisa i njihovom broju koji se nalaze u datoteci.
Moguće je filtrirati, odnosno pretražiti tablicu na način da se u polje pri dnu 
upiše ime spisa ili bilo koji dio imena spisa. Također, pretraživanje je moguće i
po broju spisa. Pretraživanje nije osjetljivo na velika, odnosno mala slova. Drugim
riječima, izraz za pretraživanje moguće je upisati kao "TEST", "test" ili bilo 
koju kombinaciju velikih i malih slova. Nakon upisanog izraza potrebno je klikunti
na gumb "Traži" da bi se u tablici prikazali isključivo filtrirani zapisi.

![Pretrazivanje](https://github.com/djurag/File_book/blob/master/data/Search_example.png)

Nakon pretraživanja moguće je ponovo prikazati cijelu tablicu pritiskom na gumb
"Ažuriraj tablicu".

## Novi spis
Pritiskom na gumb "Novi spis" otvara se novi prozor u kojem je moguće unijeti
podatke koje želite spremiti.

![Novi spis](https://github.com/djurag/File_book/blob/master/data/On_new_click.png)

Potrebno je unijeti ime spisa u obliku u kojem želite da se pohrani. Vrijednosti
u imenu mogu biti alfanumeričke, odnosno moguć je unos slova, znakova (osim znaka
zareza) te brojeva. Vrijednosti koje se unose pod "Broj spisa" moraju biti
numeričke i ništa osim brojeva nije dopušteno.

Pritiskom na tipku "Spremi" provest će se spremanje zapisa u datoteku i tablicu.
Ukoliko dođe do greške o tome ćete biti obaviješteni putem poruke na ekranu. 
Uspješan unos u tablicu i datoteku također je potvrđen putem odgovarajuće poruke.

## Ukloni spis
Opcija "Ukloni spis" omogućava uklanjanje pojedinog spisa iz tablice. Pošto
je spis jedinstveno određen brojem spisa moguće ga je ukloniti putem
broja spisa.

![Obirsi spis](https://github.com/djurag/File_book/blob/master/data/On_delete_click.png)

## Kraj rada
Kraj rada i izlazak iz programa moguć je pritiskom na tipku "Kraj rada" ili
putem naredbi operacijskog sustava (pritisak na "X").