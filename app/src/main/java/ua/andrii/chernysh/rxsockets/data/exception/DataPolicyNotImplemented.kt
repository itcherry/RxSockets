package ua.andrii.chernysh.rxsockets.data.exception

import ua.andrii.chernysh.rxsockets.data.source.DataPolicy

class DataPolicyNotImplemented(dataPolicy: DataPolicy): RuntimeException("Data policy: $dataPolicy is not implemented. Try to use another.")