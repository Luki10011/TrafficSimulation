# *A*daptive *V*ehicle System
## Czym jest Adaptive Vehicle System?

Jest to symulacja inteligentnych świateł drogowych na skrzyżowaniu, które składa się z czterech dróg dojazdowych: północ, południe, wschód i zachód. Jej przebieg definiowany jest poprzez listę komend w postaci pliku JSON. Każda komenda definiuje akcję, jaką system ma wykonać na skrzyżowaniu. Przykładowa struktura jest następująca:

```json
{
  "commands": [
    {
      "type": "addVehicle",
      "vehicleId": "vehicle4",
      "startRoad": "west",
      "endRoad": "south"
    },
    {
      "type": "step"
    },
    {
      "type": "step"
    }
  ]
}
```

* Komenda addVehicle: dodaje pojazd na wskazanej drodze początkowej (startRoad) z celem dojazdu do drogi końcowej (endRoad).
* Komenda step: wykonuje krok symulacji, podczas którego przez skrzyżowanie przejeżdżają pierwsze pojazdy na drodze, która aktualnie ma zielone światło.

Ze względu na przyjętą logikę komendy step(), jakiekolwiek aktualizacje obecnego stanu świateł drogowych, czasu oczekiwania i priorytetów muszą być wykonywane synchronicznie w ramach pojedynczego wywołania tej metody, aby zachować spójność symulacji. Oznacza to, że komenda addVehicle powoduje tylko i wyłącznie dodanie samochodu na wskaznej drodze. Wynikiem symulacji jest JSON zawierający listę pojazdów, które opuściły skrzyżowanie po każdym kroku symulacji.
```json
{"stepStatuses": [
  {"leftVehicles": ["vehicle1"]},
  {"leftVehicles": ["vehicle2"]},
  {"leftVehicles": []},
  {"leftVehicles": []},
  {"leftVehicles": []}
]}
```
## W jaki sposób działa symulacja?

Symulację można uruchomić poprzez konoslę. W tym celu należy przejść do folderu i wpisać komendę:

