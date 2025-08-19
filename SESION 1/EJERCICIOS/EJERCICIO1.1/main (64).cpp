#include <iostream>
#include <vector>
using namespace std;

int sumarElementos(const vector<int>& arreglo) {
    int suma = 0;
    for (int num : arreglo) {
        suma += num;
    }
    return suma;
}

int main() {
    vector<int> numeros = {1, 2, 3, 4, 5};
    cout << "Suma: " << sumarElementos(numeros) << endl;
    return 0;
}
