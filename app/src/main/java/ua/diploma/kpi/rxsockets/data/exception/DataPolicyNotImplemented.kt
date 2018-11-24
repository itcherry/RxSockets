package ua.diploma.kpi.rxsockets.data.exception

import ua.diploma.kpi.rxsockets.data.source.DataPolicy

class DataPolicyNotImplemented(dataPolicy: DataPolicy): RuntimeException("Data policy: $dataPolicy is not implemented. Try to use another.")